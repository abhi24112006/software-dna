package com.softwaredna.parser.language;

import java.util.List;

import com.softwaredna.graph.KnowledgeGraphBuilder;
import com.softwaredna.mapper.EntityReferenceMapper;
import com.softwaredna.model.EntityReference;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedMethod;
import com.softwaredna.model.ParsedParameter;
import com.softwaredna.model.RelationshipType;
import com.softwaredna.model.RepositoryModel;

public class PythonRelationshipExtractor {

    private final KnowledgeGraphBuilder graphBuilder;

    public PythonRelationshipExtractor() {

        graphBuilder =
                new KnowledgeGraphBuilder();
    }

    public void extractRelationships(
            RepositoryModel repository,
            List<PythonCall> calls) {

        /*
         * -------------------------------------------------------
         * Python parameter dependencies
         * -------------------------------------------------------
         *
         * Example:
         *
         * UserController.__init__(
         *     service: UserService
         * )
         *
         * becomes:
         *
         * UserController
         *        |
         *        | PARAMETER_DEPENDENCY
         *        v
         * UserService
         */
        extractParameterDependencies(
                repository
        );

        /*
         * -------------------------------------------------------
         * Python return dependencies
         * -------------------------------------------------------
         *
         * Example:
         *
         * UserService.create_user()
         *     -> User
         */
        extractReturnDependencies(
                repository
        );

        /*
         * -------------------------------------------------------
         * Python method calls
         * -------------------------------------------------------
         *
         * Example:
         *
         * self.service.create_user(user)
         *
         * becomes:
         *
         * UserController.create()
         *        |
         *        | METHOD_CALL_INTERNAL
         *        v
         * UserService.create_user()
         */
        extractMethodCalls(
                repository,
                calls
        );
    }

    private void extractParameterDependencies(
            RepositoryModel repository) {

        for (var file :
                repository.getFiles()) {

            for (ParsedClass sourceClass :
                    file.getClasses()) {

                EntityReference source =
                        EntityReferenceMapper.fromClass(
                                sourceClass
                        );

                for (ParsedMethod method :
                        sourceClass.getMethods()) {

                    for (ParsedParameter parameter :
                            method.getParameters()) {

                        /*
                         * Ignore Python's implicit self.
                         */
                        if ("self".equals(
                                parameter.getName())) {

                            continue;
                        }

                        if (parameter.getType() == null
                                || parameter.getType().isBlank()) {

                            continue;
                        }

                        ParsedClass targetClass =
                                findClass(
                                        repository,
                                        parameter.getType()
                                );

                        if (targetClass == null) {
                            continue;
                        }

                        graphBuilder.addRelationship(
                                repository,
                                source,
                                EntityReferenceMapper.fromClass(
                                        targetClass
                                ),
                                RelationshipType.PARAMETER_DEPENDENCY
                        );
                    }
                }
            }
        }
    }

    private void extractReturnDependencies(
            RepositoryModel repository) {

        for (var file :
                repository.getFiles()) {

            for (ParsedClass sourceClass :
                    file.getClasses()) {

                EntityReference source =
                        EntityReferenceMapper.fromClass(
                                sourceClass
                        );

                for (ParsedMethod method :
                        sourceClass.getMethods()) {

                    String returnType =
                            method.getReturnType();

                    if (returnType == null
                            || returnType.isBlank()) {

                        continue;
                    }

                    ParsedClass targetClass =
                            findClass(
                                    repository,
                                    returnType
                            );

                    if (targetClass == null) {
                        continue;
                    }

                    graphBuilder.addRelationship(
                            repository,
                            source,
                            EntityReferenceMapper.fromClass(
                                    targetClass
                            ),
                            RelationshipType.RETURN_DEPENDENCY
                    );
                }
            }
        }
    }

    private void extractMethodCalls(
            RepositoryModel repository,
            List<PythonCall> calls) {

        if (calls == null) {
            return;
        }

        for (PythonCall call : calls) {

            ParsedClass sourceClass =
                    findClass(
                            repository,
                            call.getSourceClass()
                    );

            if (sourceClass == null) {
                continue;
            }

            ParsedMethod sourceMethod =
                    findMethod(
                            sourceClass,
                            call.getSourceMethod()
                    );

            if (sourceMethod == null) {
                continue;
            }

            /*
             * Resolve:
             *
             * self.service
             *
             * or
             *
             * self.repository
             *
             * using constructor/method parameter types.
             */
            String targetClassName =
                    resolveReceiverType(
                            sourceClass,
                            call.getReceiver()
                    );

            if (targetClassName == null) {
                continue;
            }

            ParsedClass targetClass =
                    findClass(
                            repository,
                            targetClassName
                    );

            if (targetClass == null) {
                continue;
            }

            ParsedMethod targetMethod =
                    findMethod(
                            targetClass,
                            call.getTargetMethod()
                    );

            if (targetMethod == null) {
                continue;
            }

            graphBuilder.addRelationship(
                    repository,
                    EntityReferenceMapper.fromMethod(
                            sourceMethod
                    ),
                    EntityReferenceMapper.fromMethod(
                            targetMethod
                    ),
                    RelationshipType.METHOD_CALL_INTERNAL
            );
        }
    }

    private String resolveReceiverType(
            ParsedClass sourceClass,
            String receiver) {

        if (receiver == null
                || receiver.isBlank()) {

            return null;
        }

        /*
         * We currently expect receivers such as:
         *
         * self.service
         * self.repository
         *
         * Extract:
         *
         * service
         * repository
         */
        String variableName =
                receiver;

        if (variableName.startsWith(
                "self."
        )) {

            variableName =
                    variableName.substring(
                            "self.".length()
                    );
        }

        /*
         * Search every method parameter.
         *
         * This works for the current Python test
         * project because dependencies are injected
         * through constructors.
         */
        for (ParsedMethod method :
                sourceClass.getMethods()) {

            for (ParsedParameter parameter :
                    method.getParameters()) {

                if (parameter.getName()
                        .equals(variableName)) {

                    return parameter.getType();
                }
            }
        }

        return null;
    }

    private ParsedClass findClass(
            RepositoryModel repository,
            String className) {

        if (className == null
                || className.isBlank()) {

            return null;
        }

        /*
         * Strip generic syntax if present.
         *
         * Example:
         *
         * List[User]
         *
         * is not a direct class name.
         */
        String normalized =
                className.trim();

        int genericIndex =
                normalized.indexOf('[');

        if (genericIndex >= 0) {

            normalized =
                    normalized.substring(
                            0,
                            genericIndex
                    );
        }

        for (var file :
                repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                if (normalized.equals(
                        parsedClass.getName()
                )) {

                    return parsedClass;
                }
            }
        }

        return null;
    }

    private ParsedMethod findMethod(
            ParsedClass parsedClass,
            String methodName) {

        if (parsedClass == null
                || methodName == null) {

            return null;
        }

        for (ParsedMethod method :
                parsedClass.getMethods()) {

            if (methodName.equals(
                    method.getName()
            )) {

                return method;
            }
        }

        return null;
    }
}