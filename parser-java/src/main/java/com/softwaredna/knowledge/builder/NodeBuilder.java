package com.softwaredna.knowledge.builder;

import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedConstructor;
import com.softwaredna.model.ParsedField;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.ParsedInterface;
import com.softwaredna.model.ParsedMethod;
import com.softwaredna.model.RepositoryModel;

import java.util.HashSet;
import java.util.Set;

public class NodeBuilder {

    public void buildNodes(
            RepositoryModel repository,
            KnowledgeGraph graph) {

        buildPackageNodes(
                repository,
                graph);

        buildClassNodes(
                repository,
                graph);

        buildInterfaceNodes(
                repository,
                graph);

        buildMethodNodes(
                repository,
                graph);

        buildFieldNodes(
                repository,
                graph);

        buildConstructorNodes(
                repository,
                graph);

    }

    /*
     * --------------------------
     * Packages
     * --------------------------
     */

    private void buildPackageNodes(
            RepositoryModel repository,
            KnowledgeGraph graph) {

        Set<String> packages =
                new HashSet<>();

        for (ParsedFile file :
                repository.getFiles()) {

            String packageName =
                    file.getPackageName();

            if (packageName == null
                    || packageName.isBlank()) {

                packageName =
                        "Default Package";

            }

            if (!packages.add(packageName)) {
                continue;
            }

            graph.addNode(

                    new GraphNode(

                            "PACKAGE:" + packageName,

                            packageName,

                            NodeType.PACKAGE

                    )

            );

        }

    }

    /*
     * --------------------------
     * Classes
     * --------------------------
     */

    private void buildClassNodes(
            RepositoryModel repository,
            KnowledgeGraph graph) {

        for (ParsedFile file :
                repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                graph.addNode(

                        new GraphNode(

                                parsedClass.getId(),

                                parsedClass.getName(),

                                NodeType.CLASS

                        )

                );

            }

        }

    }

    /*
     * --------------------------
     * Interfaces
     * --------------------------
     */

    private void buildInterfaceNodes(
            RepositoryModel repository,
            KnowledgeGraph graph) {

        for (ParsedFile file :
                repository.getFiles()) {

            for (ParsedInterface parsedInterface :
                    file.getInterfaces()) {

                if (parsedInterface.getId() == null) {
                    continue;
                }

                graph.addNode(

                        new GraphNode(

                                parsedInterface.getId(),

                                parsedInterface.getName(),

                                NodeType.INTERFACE

                        )

                );

            }

        }

    }

    /*
     * --------------------------
     * Methods
     * --------------------------
     */

    private void buildMethodNodes(
            RepositoryModel repository,
            KnowledgeGraph graph) {

        for (ParsedFile file :
                repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                for (ParsedMethod method :
                        parsedClass.getMethods()) {

                    graph.addNode(

                            new GraphNode(

                                    method.getId(),

                                    parsedClass.getName()
                                            + "."
                                            + method.getName()
                                            + "()",

                                    NodeType.METHOD

                            )

                    );

                }

            }

        }

    }

    /*
     * --------------------------
     * Fields
     * --------------------------
     */

    private void buildFieldNodes(
            RepositoryModel repository,
            KnowledgeGraph graph) {

        for (ParsedFile file :
                repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                for (ParsedField field :
                        parsedClass.getFields()) {

                    graph.addNode(

                            new GraphNode(

                                    field.getId(),

                                    parsedClass.getName()
                                            + "."
                                            + field.getName(),

                                    NodeType.FIELD

                            )

                    );

                }

            }

        }

    }

    /*
     * --------------------------
     * Constructors
     * --------------------------
     */

    private void buildConstructorNodes(
            RepositoryModel repository,
            KnowledgeGraph graph) {

        for (ParsedFile file :
                repository.getFiles()) {

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                for (ParsedConstructor constructor :
                        parsedClass.getConstructors()) {

                    graph.addNode(

                            new GraphNode(

                                    constructor.getId(),

                                    parsedClass.getName()
                                            + "()",

                                    NodeType.CONSTRUCTOR

                            )

                    );

                }

            }

        }

    }

}