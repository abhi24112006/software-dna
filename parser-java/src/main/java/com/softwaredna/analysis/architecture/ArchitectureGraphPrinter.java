package com.softwaredna.analysis.architecture;

import java.util.HashMap;
import java.util.Map;

public class ArchitectureGraphPrinter {

    public void print(
            ArchitectureGraph graph) {

        System.out.println();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Architecture Dependency Graph"
        );

        System.out.println(
                "======================================"
        );


        /*
         * ===================================================
         * Nodes grouped by architecture layer
         * ===================================================
         */

        Map<ArchitectureLayer, Integer>
                layerCounts =
                new HashMap<>();


        for (ArchitectureGraphNode node :
                graph.getNodes()) {

            ArchitectureLayer layer =
                    node.getLayer();

            layerCounts.put(
                    layer,
                    layerCounts.getOrDefault(
                            layer,
                            0
                    ) + 1
            );

        }


        System.out.println();

        System.out.println(
                "Nodes:"
        );


        for (ArchitectureGraphNode node :
                graph.getNodes()) {

            System.out.println(
                    "  "
                            + node.getLayer()
                            + " -> "
                            + node.getName()
            );

        }


        /*
         * ===================================================
         * Dependency edges
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "Dependencies:"
        );


        if (graph.getEdges().isEmpty()) {

            System.out.println(
                    "  None"
            );

        }
        else {

            for (ArchitectureGraphEdge edge :
                    graph.getEdges()) {

                System.out.println(
                        "  "
                                + edge.getSource()
                                + " --"
                                + edge.getRelationship()
                                + "--> "
                                + edge.getTarget()
                );

            }

        }


        /*
         * ===================================================
         * Summary
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "Graph Summary:"
        );

        System.out.println(
                "  Nodes : "
                        + graph.getNodes().size()
        );

        System.out.println(
                "  Edges : "
                        + graph.getEdges().size()
        );


        System.out.println();

    }

}