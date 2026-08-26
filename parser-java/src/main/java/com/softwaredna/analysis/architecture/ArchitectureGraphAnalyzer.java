package com.softwaredna.analysis.architecture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.softwaredna.graph.GraphRepository;

public class ArchitectureGraphAnalyzer {

    private final GraphRepository graphRepository;


    public ArchitectureGraphAnalyzer(
            GraphRepository graphRepository) {

        this.graphRepository =
                graphRepository;

    }


    public ArchitectureGraph build(
            ArchitectureReport report) {

        List<ArchitectureGraphNode> nodes =
                new ArrayList<>();

        List<ArchitectureGraphEdge> edges =
                new ArrayList<>();


        /*
         * ===================================================
         * Nodes
         * ===================================================
         */

        for (Map.Entry<String, ArchitectureLayer>
                entry : report.getLayers().entrySet()) {

            String nodeId =
                    entry.getKey();

            ArchitectureLayer layer =
                    entry.getValue();


            if (layer ==
                    ArchitectureLayer.UNKNOWN) {

                continue;

            }


            nodes.add(
                    new ArchitectureGraphNode(
                            nodeId,
                            displayName(nodeId),
                            layer
                    )
            );

        }


        /*
         * ===================================================
         * Edges
         * ===================================================
         *
         * ArchitectureEvidence already contains the
         * relationships discovered by ArchitectureAnalyzer.
         *
         * Therefore we use that evidence directly instead
         * of querying Neo4j again.
         */

        Set<String> processedEdges =
                new HashSet<>();


        for (ArchitectureEvidence item :
                report.getEvidence()) {

            String source =
                    item.getSource();

            String target =
                    item.getTarget();


            String edgeKey =
                    source
                            + "->"
                            + target;


            /*
             * Prevent duplicate edges.
             */

            if (!processedEdges.add(
                    edgeKey)) {

                continue;

            }


            edges.add(
                    new ArchitectureGraphEdge(
                            source,
                            target,
                            item.getRelationship()
                    )
            );

        }


        return new ArchitectureGraph(
                nodes,
                edges
        );

    }


    /*
     * ===================================================
     * Display Helpers
     * ===================================================
     */

    private String displayName(
            String nodeId) {

        if (nodeId == null) {

            return "";

        }


        int hashIndex =
                nodeId.lastIndexOf('#');


        if (hashIndex >= 0) {

            String owner =
                    nodeId.substring(
                            0,
                            hashIndex
                    );

            String method =
                    nodeId.substring(
                            hashIndex
                    );

            return simpleName(owner)
                    + method;

        }


        return simpleName(nodeId);

    }


    private String simpleName(
            String value) {

        int dotIndex =
                value.lastIndexOf('.');


        if (dotIndex >= 0
                && dotIndex <
                value.length() - 1) {

            return value.substring(
                    dotIndex + 1
            );

        }


        return value;

    }

}