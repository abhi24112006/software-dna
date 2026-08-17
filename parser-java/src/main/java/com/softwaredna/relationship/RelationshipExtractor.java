package com.softwaredna.relationship;

import com.softwaredna.analysis.MethodAnalysisResult;
import com.softwaredna.model.ParsedMethodCall;
import com.softwaredna.resolver.MethodResolver;
import com.softwaredna.resolver.ReceiverResolver;
import com.softwaredna.graph.KnowledgeGraphBuilder;
import com.softwaredna.mapper.EntityReferenceMapper;
import com.softwaredna.model.EntityReference;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedField;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.ParsedInterface;
import com.softwaredna.model.ParsedMethod;
import com.softwaredna.model.ParsedParameter;
import com.softwaredna.model.RelationshipType;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.resolver.EntityResolver;
import com.softwaredna.type.TypeReferenceExtractor;

public class RelationshipExtractor {

    private final EntityResolver resolver =
            new EntityResolver();

    private final TypeReferenceExtractor typeExtractor =
            new TypeReferenceExtractor();

    private final KnowledgeGraphBuilder graphBuilder =
            new KnowledgeGraphBuilder();

    private final ReceiverResolver receiverResolver =
            new ReceiverResolver();

    private final MethodResolver methodResolver =
            new MethodResolver();


    /*
     * -------------------------------------------------------
     * Extract All Relationships
     * -------------------------------------------------------
     */

    public void extractRelationships(
            RepositoryModel repository) {

        extractExtends(repository);

        extractImplements(repository);

        extractFieldDependencies(repository);

        extractParameterDependencies(repository);

        extractReturnDependencies(repository);

        extractMethodCalls(repository);

    }


    /*
     * -------------------------------------------------------
     * EXTENDS
     * -------------------------------------------------------
     */

    private void extractExtends(
            RepositoryModel repository) {

        for (ParsedFile file :
                repository.getFiles()) {

            for (ParsedClass child :
                    file.getClasses()) {

                if (child.getSuperClass() == null
                        || child.getSuperClass().isBlank()) {

                    continue;

                }

                ParsedClass parent =
                        resolver.resolveClass(
                                child.getSuperClass(),
                                file.getPackageName(),
                                file.getImports(),
                                repository.getEntityRegistry()
                        );

                if (parent == null) {
                    continue;
                }

                graphBuilder.addRelationship(
                        repository,
                        EntityReferenceMapper.fromClass(child),
                        EntityReferenceMapper.fromClass(parent),
                        RelationshipType.EXTENDS
                );

            }

        }

    }


    /*
     * -------------------------------------------------------
     * IMPLEMENTS
     * -------------------------------------------------------
     */

    private void extractImplements(
            RepositoryModel repository) {

        for (ParsedFile file :
                repository.getFiles()) {

            String packageName =
                    file.getPackageName();

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                EntityReference source =
                        EntityReferenceMapper.fromClass(
                                parsedClass);

                for (String interfaceName :
                        parsedClass.getImplementedInterfaces()) {

                    ParsedInterface parsedInterface =
                            resolver.resolveInterface(
                                    interfaceName,
                                    packageName,
                                    file.getImports(),
                                    repository.getEntityRegistry()
                            );

                    /*
                     * If interface cannot be resolved
                     * unambiguously, do not create a
                     * false relationship.
                     */

                    if (parsedInterface == null) {
                        continue;
                    }

                    graphBuilder.addRelationship(
                            repository,
                            source,
                            EntityReferenceMapper.fromInterface(
                                    parsedInterface),
                            RelationshipType.IMPLEMENTS
                    );

                }

            }

        }

    }


    /*
     * -------------------------------------------------------
     * FIELD DEPENDENCIES
     * -------------------------------------------------------
     */

    private void extractFieldDependencies(
            RepositoryModel repository) {

        for (ParsedFile file :
                repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                EntityReference source =
                        EntityReferenceMapper.fromClass(
                                parsedClass);

                for (ParsedField field :
                        parsedClass.getFields()) {

                    addTypeDependencies(
                            repository,
                            source,
                            field.getType(),
                            file.getPackageName(),
                            file.getImports(),
                            RelationshipType.FIELD_DEPENDENCY
                    );

                }

            }

        }

    }


    /*
     * -------------------------------------------------------
     * PARAMETER DEPENDENCIES
     * -------------------------------------------------------
     */

    private void extractParameterDependencies(
            RepositoryModel repository) {

        for (ParsedFile file :
                repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                EntityReference source =
                        EntityReferenceMapper.fromClass(
                                parsedClass);

                for (ParsedMethod method :
                        parsedClass.getMethods()) {

                    for (ParsedParameter parameter :
                            method.getParameters()) {

                        addTypeDependencies(
                                repository,
                                source,
                                parameter.getType(),
                                file.getPackageName(),
                                file.getImports(),
                                RelationshipType.PARAMETER_DEPENDENCY
                        );

                    }

                }

            }

        }

    }


    /*
     * -------------------------------------------------------
     * RETURN DEPENDENCIES
     * -------------------------------------------------------
     */

    private void extractReturnDependencies(
            RepositoryModel repository) {

        for (ParsedFile file :
                repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                EntityReference source =
                        EntityReferenceMapper.fromClass(
                                parsedClass);

                for (ParsedMethod method :
                        parsedClass.getMethods()) {

                    addTypeDependencies(
                            repository,
                            source,
                            method.getReturnType(),
                            file.getPackageName(),
                            file.getImports(),
                            RelationshipType.RETURN_DEPENDENCY
                    );

                }

            }

        }

    }


    /*
     * -------------------------------------------------------
     * METHOD CALLS
     * -------------------------------------------------------
     */

    private void extractMethodCalls(
            RepositoryModel repository) {

        for (ParsedFile file :
                repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                for (ParsedMethod method :
                        parsedClass.getMethods()) {

                    MethodAnalysisResult analysisResult =
                            method.getAnalysisResult();

                    if (analysisResult == null) {
                        continue;
                    }

                    for (ParsedMethodCall methodCall :
                            analysisResult.getMethodCalls()) {

                        String receiverType =
                                receiverResolver.resolveReceiverType(
                                        methodCall.getReceiverExpression(),
                                        parsedClass,
                                        analysisResult.getScope()
                                );

                        if (receiverType == null) {
                            continue;
                        }

                        ParsedMethod targetMethod =
                                methodResolver.resolveMethod(
                                        receiverType,
                                        methodCall,
                                        repository.getEntityRegistry(),
                                        parsedClass
                                );

                        if (targetMethod == null) {
                            continue;
                        }

                        graphBuilder.addRelationship(
                                repository,
                                EntityReferenceMapper.fromMethod(
                                        method),
                                EntityReferenceMapper.fromMethod(
                                        targetMethod),
                                RelationshipType.METHOD_CALL_INTERNAL
                        );

                    }

                }

            }

        }

    }


    /*
     * -------------------------------------------------------
     * COMMON TYPE DEPENDENCY HELPER
     * -------------------------------------------------------
     */

    private void addTypeDependencies(
            RepositoryModel repository,
            EntityReference source,
            String type,
            String packageName,
            java.util.List<String> imports,
            RelationshipType relationshipType) {

        for (String referencedType :
                typeExtractor.extractReferencedTypes(type)) {

            EntityReference target =
                    resolver.resolveType(
                            referencedType,
                            packageName,
                            imports,
                            repository.getEntityRegistry()
                    );

            if (target == null) {
                continue;
            }

            graphBuilder.addRelationship(
                    repository,
                    source,
                    target,
                    relationshipType
            );

        }

    }

}