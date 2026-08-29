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

public class JavaScriptRelationshipExtractor {

    private final KnowledgeGraphBuilder graphBuilder;

    public JavaScriptRelationshipExtractor() {

        graphBuilder = new KnowledgeGraphBuilder();

    }

    public void extractRelationships(
            RepositoryModel repository,
            List<JavaScriptCall> calls) {

        /*
         * Phase 1
         * Extract inheritance relationships.
         */
        extractInheritance(repository);

        /*
         * Phase 2
         * Extract parameter dependencies.
         */
        extractParameterDependencies(repository);

        /*
         * Phase 3
         * Extract method calls and class-level
         * dependencies from those calls.
         */
        extractMethodCalls(repository, calls);

    }


    /*
     * ============================================================
     * INHERITANCE
     * ============================================================
     */

    private void extractInheritance(
            RepositoryModel repository) {

        for (var file : repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                String superClass =
                        parsedClass.getSuperClass();

                if (superClass == null ||
                        superClass.isBlank()) {

                    continue;
                }

                ParsedClass target =
                        findClass(
                                repository,
                                superClass
                        );

                if (target == null) {

                    continue;
                }

                graphBuilder.addRelationship(
                        repository,

                        EntityReferenceMapper.fromClass(
                                parsedClass
                        ),

                        EntityReferenceMapper.fromClass(
                                target
                        ),

                        RelationshipType.EXTENDS
                );
            }
        }
    }


    /*
     * ============================================================
     * PARAMETER DEPENDENCIES
     * ============================================================
     */

    private void extractParameterDependencies(
            RepositoryModel repository) {

        for (var file : repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                EntityReference source =
                        EntityReferenceMapper.fromClass(
                                parsedClass
                        );

                for (ParsedMethod method :
                        parsedClass.getMethods()) {

                    for (ParsedParameter parameter :
                            method.getParameters()) {

                        if (parameter.getType() == null ||
                                parameter.getType().isBlank()) {

                            continue;
                        }

                        ParsedClass target =
                                findClass(
                                        repository,
                                        parameter.getType()
                                );

                        if (target == null) {

                            continue;
                        }

                        graphBuilder.addRelationship(
                                repository,

                                source,

                                EntityReferenceMapper.fromClass(
                                        target
                                ),

                                RelationshipType.PARAMETER_DEPENDENCY
                        );
                    }
                }
            }
        }
    }


    /*
     * ============================================================
     * METHOD CALLS + CLASS DEPENDENCIES
     * ============================================================
     */

    private void extractMethodCalls(
            RepositoryModel repository,
            List<JavaScriptCall> calls) {

        if (calls == null) {

            return;
        }

        for (JavaScriptCall call : calls) {

            /*
             * Find source class.
             */
            ParsedClass sourceClass =
                    findClass(
                            repository,
                            call.getSourceClass()
                    );

            if (sourceClass == null) {

                continue;
            }

            /*
             * Find source method.
             */
            ParsedMethod sourceMethod =
                    findMethod(
                            sourceClass,
                            call.getSourceMethod()
                    );

            if (sourceMethod == null) {

                continue;
            }

            /*
             * Resolve the class represented by
             * the receiver.
             *
             * Example:
             *
             * this.service.createUser()
             *
             * receiver = this.service
             *
             * -> UserService
             */
            String targetClassName =
                    resolveReceiverType(
                            repository,
                            sourceClass,
                            call.getReceiver()
                    );

            if (targetClassName == null) {

                continue;
            }

            /*
             * Find target class.
             */
            ParsedClass targetClass =
                    findClass(
                            repository,
                            targetClassName
                    );

            if (targetClass == null) {

                continue;
            }

            /*
             * ----------------------------------------------------
             * IMPORTANT:
             *
             * Add CLASS -> CLASS dependency.
             *
             * This is what Architecture Recovery needs.
             *
             * Example:
             *
             * UserController -> UserService
             * UserService -> UserRepository
             * ----------------------------------------------------
             */

            graphBuilder.addRelationship(
                    repository,

                    EntityReferenceMapper.fromClass(
                            sourceClass
                    ),

                    EntityReferenceMapper.fromClass(
                            targetClass
                    ),

                    RelationshipType.FIELD_DEPENDENCY
            );

            /*
             * Find target method.
             */
            ParsedMethod targetMethod =
                    findMethod(
                            targetClass,
                            call.getTargetMethod()
                    );

            if (targetMethod == null) {

                continue;
            }

            /*
             * ----------------------------------------------------
             * Add METHOD -> METHOD call relationship.
             * ----------------------------------------------------
             */

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


    /*
     * ============================================================
     * RECEIVER TYPE RESOLUTION
     * ============================================================
     */

    private String resolveReceiverType(
            RepositoryModel repository,
            ParsedClass sourceClass,
            String receiver) {

        if (receiver == null ||
                receiver.isBlank()) {

            return null;
        }

        String variableName =
                receiver;

        /*
         * Convert:
         *
         * this.service
         *
         * into:
         *
         * service
         */
        if (variableName.startsWith("this.")) {

            variableName =
                    variableName.substring(
                            "this.".length()
                    );
        }

        /*
         * ----------------------------------------------------
         * 1. Try typed parameters.
         *
         * Supports TypeScript:
         *
         * constructor(service: UserService)
         * ----------------------------------------------------
         */

        for (ParsedMethod method :
                sourceClass.getMethods()) {

            for (ParsedParameter parameter :
                    method.getParameters()) {

                if (!parameter.getName()
                        .equals(variableName)) {

                    continue;
                }

                if (parameter.getType() != null &&
                        !parameter.getType().isBlank()) {

                    return parameter.getType();
                }
            }
        }

        /*
         * ----------------------------------------------------
         * 2. Plain JavaScript.
         *
         * JavaScript parameters do not contain type
         * information, so inspect imports.
         *
         * Example:
         *
         * import UserService from "../service/UserService.js"
         *
         * variable:
         *
         * service
         *
         * class:
         *
         * UserService
         * ----------------------------------------------------
         */

        for (var file : repository.getFiles()) {

            boolean ownsClass = false;

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                if (parsedClass == sourceClass) {

                    ownsClass = true;

                    break;
                }
            }

            if (!ownsClass) {

                continue;
            }

            for (String imported :
                    file.getImports()) {

                String importedClass =
                        extractClassName(
                                imported
                        );

                if (importedClass == null) {

                    continue;
                }

                String normalizedVariable =
                        variableName
                                .toLowerCase();

                String normalizedClass =
                        importedClass
                                .toLowerCase();

                /*
                 * Example:
                 *
                 * service
                 * UserService
                 *
                 * repository
                 * UserRepository
                 */

                if (normalizedClass.contains(
                        normalizedVariable
                )) {

                    return importedClass;
                }
            }
        }

        /*
         * ----------------------------------------------------
         * 3. Final fallback.
         *
         * Search all known classes.
         * ----------------------------------------------------
         */

        for (var file : repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                String className =
                        parsedClass.getName();

                String normalizedClass =
                        className.toLowerCase();

                String normalizedVariable =
                        variableName.toLowerCase();

                if (normalizedClass.contains(
                        normalizedVariable
                )) {

                    return className;
                }
            }
        }

        return null;
    }


    /*
     * ============================================================
     * IMPORT CLASS NAME
     * ============================================================
     */

    private String extractClassName(
            String importPath) {

        if (importPath == null ||
                importPath.isBlank()) {

            return null;
        }

        String value =
                importPath;

        int slash =
                Math.max(
                        value.lastIndexOf('/'),
                        value.lastIndexOf('\\')
                );

        if (slash >= 0) {

            value =
                    value.substring(
                            slash + 1
                    );
        }

        if (value.endsWith(".tsx")) {

            value =
                    value.substring(
                            0,
                            value.length() - 4
                    );
        }

        else if (value.endsWith(".jsx")) {

            value =
                    value.substring(
                            0,
                            value.length() - 4
                    );
        }

        else if (value.endsWith(".ts")) {

            value =
                    value.substring(
                            0,
                            value.length() - 3
                    );
        }

        else if (value.endsWith(".js")) {

            value =
                    value.substring(
                            0,
                            value.length() - 3
                    );
        }

        return value.isBlank()
                ? null
                : value;
    }


    /*
     * ============================================================
     * FIND CLASS
     * ============================================================
     */

    private ParsedClass findClass(
            RepositoryModel repository,
            String className) {

        if (className == null ||
                className.isBlank()) {

            return null;
        }

        String normalized =
                className.trim();

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


    /*
     * ============================================================
     * FIND METHOD
     * ============================================================
     */

    private ParsedMethod findMethod(
            ParsedClass parsedClass,
            String methodName) {

        if (parsedClass == null ||
                methodName == null) {

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