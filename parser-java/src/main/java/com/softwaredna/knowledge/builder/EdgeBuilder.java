package com.softwaredna.knowledge.builder;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedConstructor;
import com.softwaredna.model.ParsedField;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.ParsedMethod;
import com.softwaredna.model.Relationship;
import com.softwaredna.model.RelationshipType;
import com.softwaredna.model.RepositoryModel;

public class EdgeBuilder {

    public void buildEdges(
        RepositoryModel repository,
        KnowledgeGraph graph) {

    buildPackageClassEdges(
            repository,
            graph);

    buildClassMethodEdges(
            repository,
            graph);

    buildClassFieldEdges(
            repository,
            graph);

    buildClassConstructorEdges(
            repository,
            graph);

    buildMethodCallEdges(
            repository,
            graph);

    buildInheritanceEdges(
            repository,
            graph);

    buildDependencyEdges(
            repository,
            graph);
}

    /*
     * --------------------------
     * Package -> Class
     * --------------------------
     */

    private void buildPackageClassEdges(
            RepositoryModel repository,
            KnowledgeGraph graph) {

        for (ParsedFile file :
                repository.getFiles()) {

            String packageName =
                    file.getPackageName();

            if (packageName == null) {

                packageName =
                        "Default Package";

            }

            GraphNode packageNode =
                    graph.getNode(
                            "PACKAGE:" + packageName);

            if (packageNode == null) {
                continue;
            }

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                GraphNode classNode =
                        graph.getNode(
                                parsedClass.getId());

                if (classNode == null) {
                    continue;
                }

                graph.addEdge(
                        new GraphEdge(
                                packageNode,
                                classNode,
                                EdgeType.DECLARES
                        )
                );

            }

        }

    }

    /*
     * --------------------------
     * Class -> Method
     * --------------------------
     */

    private void buildClassMethodEdges(
            RepositoryModel repository,
            KnowledgeGraph graph) {

        for (ParsedFile file :
                repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                GraphNode classNode =
                        graph.getNode(
                                parsedClass.getId());

                if (classNode == null) {
                    continue;
                }

                for (ParsedMethod method :
                        parsedClass.getMethods()) {

                    GraphNode methodNode =
                            graph.getNode(
                                    method.getId());

                    if (methodNode == null) {
                        continue;
                    }

                    graph.addEdge(
                            new GraphEdge(
                                    classNode,
                                    methodNode,
                                    EdgeType.HAS_METHOD
                            )
                    );

                }

            }

        }

    }

    /*
     * --------------------------
     * Class -> Field
     * --------------------------
     */

    private void buildClassFieldEdges(
            RepositoryModel repository,
            KnowledgeGraph graph) {

        for (ParsedFile file :
                repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                GraphNode classNode =
                        graph.getNode(
                                parsedClass.getId());

                if (classNode == null) {
                    continue;
                }

                for (ParsedField field :
                        parsedClass.getFields()) {

                    GraphNode fieldNode =
                            graph.getNode(
                                    field.getId());

                    if (fieldNode == null) {
                        continue;
                    }

                    graph.addEdge(
                            new GraphEdge(
                                    classNode,
                                    fieldNode,
                                    EdgeType.HAS_FIELD
                            )
                    );

                }

            }

        }

    }

    /*
     * --------------------------
     * Class -> Constructor
     * --------------------------
     */

    private void buildClassConstructorEdges(
            RepositoryModel repository,
            KnowledgeGraph graph) {

        for (ParsedFile file :
                repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                GraphNode classNode =
                        graph.getNode(
                                parsedClass.getId());

                if (classNode == null) {
                    continue;
                }

                for (ParsedConstructor constructor :
                        parsedClass.getConstructors()) {

                    GraphNode constructorNode =
                            graph.getNode(
                                    constructor.getId());

                    if (constructorNode == null) {
                        continue;
                    }

                    graph.addEdge(
                            new GraphEdge(
                                    classNode,
                                    constructorNode,
                                    EdgeType.HAS_CONSTRUCTOR
                            )
                    );

                }

            }

        }

    }

    /*
     * --------------------------
     * Method -> Method
     * CALLS
     * --------------------------
     */

    private void buildMethodCallEdges(
            RepositoryModel repository,
            KnowledgeGraph graph) {

        for (Relationship relationship :
                repository.getRelationships()) {

            if (relationship.getType()
                    != RelationshipType.METHOD_CALL_INTERNAL) {

                continue;
            }

            GraphNode sourceNode =
                    graph.getNode(
                            relationship
                                    .getSource()
                                    .getId());

            GraphNode targetNode =
                    graph.getNode(
                            relationship
                                    .getTarget()
                                    .getId());

            if (sourceNode == null
                    || targetNode == null) {

                continue;
            }

            if (sourceNode.getType()
                    != NodeType.METHOD) {

                continue;
            }

            if (targetNode.getType()
                    != NodeType.METHOD) {

                continue;
            }

            graph.addEdge(
                    new GraphEdge(
                            sourceNode,
                            targetNode,
                            EdgeType.CALLS
                    )
            );

        }

    }

    /*
     * --------------------------
     * Inheritance
     * --------------------------
     *
     * EXTENDS:
     *
     * Child Class
     *      |
     *      | EXTENDS
     *      ▼
     * Parent Class
     *
     * IMPLEMENTS:
     *
     * Class
     *   |
     *   | IMPLEMENTS
     *   ▼
     * Interface
     */

    private void buildInheritanceEdges(
            RepositoryModel repository,
            KnowledgeGraph graph) {

        for (Relationship relationship :
                repository.getRelationships()) {

            RelationshipType relationshipType =
                    relationship.getType();

            if (relationshipType
                    != RelationshipType.EXTENDS
                    && relationshipType
                    != RelationshipType.IMPLEMENTS) {

                continue;
            }

            GraphNode sourceNode =
                    graph.getNode(
                            relationship
                                    .getSource()
                                    .getId());

            GraphNode targetNode =
                    graph.getNode(
                            relationship
                                    .getTarget()
                                    .getId());

            if (sourceNode == null
                    || targetNode == null) {

                continue;
            }

            EdgeType edgeType;

            if (relationshipType
                    == RelationshipType.EXTENDS) {

                edgeType =
                        EdgeType.EXTENDS;

            } else {

                edgeType =
                        EdgeType.IMPLEMENTS;

            }

            graph.addEdge(
                    new GraphEdge(
                            sourceNode,
                            targetNode,
                            edgeType
                    )
            );

        }

    }

    /*
 * --------------------------
 * Class -> Class / Interface
 * DEPENDS_ON
 * --------------------------
 *
 * Converts detailed dependency
 * relationships into high-level
 * architectural dependency edges.
 *
 * FIELD_DEPENDENCY
 * PARAMETER_DEPENDENCY
 * RETURN_DEPENDENCY
 *
 *        |
 *        v
 *
 *      DEPENDS_ON
 */

/*
 * --------------------------
 * Class -> Class / Interface
 * DEPENDS_ON
 * --------------------------
 *
 * FIELD_DEPENDENCY
 * PARAMETER_DEPENDENCY
 * RETURN_DEPENDENCY
 *
 * are converted into a single
 * high-level DEPENDS_ON graph edge.
 */

private void buildDependencyEdges(
        RepositoryModel repository,
        KnowledgeGraph graph) {

    for (Relationship relationship :
            repository.getRelationships()) {

        RelationshipType relationshipType =
                relationship.getType();

        /*
         * Only these relationship types
         * represent type dependencies.
         */

        if (relationshipType
                != RelationshipType.FIELD_DEPENDENCY
                && relationshipType
                != RelationshipType.PARAMETER_DEPENDENCY
                && relationshipType
                != RelationshipType.RETURN_DEPENDENCY) {

            continue;
        }

        GraphNode sourceNode =
                graph.getNode(
                        relationship
                                .getSource()
                                .getId());

        GraphNode targetNode =
                graph.getNode(
                        relationship
                                .getTarget()
                                .getId());

        if (sourceNode == null
                || targetNode == null) {

            continue;
        }

        /*
         * DEPENDS_ON is a high-level
         * class/interface relationship.
         */

        if (sourceNode.getType()
                != NodeType.CLASS) {

            continue;
        }

        if (targetNode.getType()
                != NodeType.CLASS
                && targetNode.getType()
                != NodeType.INTERFACE) {

            continue;
        }

        graph.addEdge(
                new GraphEdge(
                        sourceNode,
                        targetNode,
                        EdgeType.DEPENDS_ON
                )
        );
    }
}

}