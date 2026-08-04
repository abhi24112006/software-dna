package com.softwaredna.knowledge.builder;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.model.ParsedMethod;
import com.softwaredna.model.ParsedField;
import com.softwaredna.model.ParsedConstructor;

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

}