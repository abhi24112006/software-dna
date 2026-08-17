package com.softwaredna.knowledge.query;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphNode;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class ImpactAnalyzer {

    private final KnowledgeGraphQuery query;


    public ImpactAnalyzer(
            KnowledgeGraphQuery query) {

        this.query = query;

    }


    /*
     * -------------------------------------------------------
     * Default Impact Analysis
     * -------------------------------------------------------
     *
     * Uses DEPENDS_ON relationships only.
     *
     * Example:
     *
     * Student
     *    ↑
     *    |
     * DEPENDS_ON
     *    |
     * StudentService
     *
     */

    public List<GraphNode> getImpact(
            String nodeId) {

        Set<EdgeType> defaultTypes =
                new LinkedHashSet<>();

        defaultTypes.add(
                EdgeType.DEPENDS_ON
        );

        return getImpact(
                nodeId,
                defaultTypes
        );

    }


    /*
     * -------------------------------------------------------
     * Configurable Impact Analysis
     * -------------------------------------------------------
     */

    public List<GraphNode> getImpact(
            String nodeId,
            Set<EdgeType> relationshipTypes) {

        return bfsImpact(
                nodeId,
                relationshipTypes
        );

    }


    /*
     * -------------------------------------------------------
     * Containment-Aware Impact Analysis
     * -------------------------------------------------------
     *
     * Understands:
     *
     * Class
     *    |
     *    | HAS_METHOD
     *    ↓
     * Method
     *
     * Method
     *    ↑
     *    | CALLS
     *    |
     * Caller Method
     *
     * Caller Method
     *    ↑
     *    | HAS_METHOD
     *    |
     * Caller Class
     *
     * Caller Class
     *    ↑
     *    | DEPENDS_ON
     *    |
     * Dependent Class
     *
     */

    public List<GraphNode> getContainmentAwareImpact(
        String nodeId) {

    List<GraphNode> impactedNodes =
            new ArrayList<>();

    GraphNode startNode =
            query.findNodeById(nodeId);

    if (startNode == null) {
        return impactedNodes;
    }

    Queue<GraphNode> queue =
            new LinkedList<>();

    Set<String> visited =
            new LinkedHashSet<>();


    /*
     * -------------------------------------------------------
     * Start
     * -------------------------------------------------------
     */

    queue.add(startNode);

    visited.add(
            startNode.getId()
    );


    /*
     * -------------------------------------------------------
     * BFS
     * -------------------------------------------------------
     */

    while (!queue.isEmpty()) {

        GraphNode current =
                queue.poll();


        /*
         * ===================================================
         * CLASS
         * ===================================================
         */

        if (current.getType().name().equals("CLASS")) {

            /*
             * ------------------------------------------------
             * Find classes that depend on this class.
             * ------------------------------------------------
             */

            addIncomingNodes(
                    current,
                    EdgeType.DEPENDS_ON,
                    queue,
                    visited,
                    impactedNodes
            );


            /*
             * ------------------------------------------------
             * Only expand the ORIGINAL changed class into
             * its methods.
             *
             * Example:
             *
             * Teacher
             *    |
             *    | HAS_METHOD
             *    v
             * Teacher.teach()
             *
             * But once we reach StudentService, we DO NOT
             * expand StudentService into all its methods.
             * ------------------------------------------------
             */

            if (current.getId().equals(nodeId)) {

                addOutgoingNodes(
                        current,
                        EdgeType.HAS_METHOD,
                        queue,
                        visited,
                        impactedNodes
                );

            }

        }


        /*
         * ===================================================
         * METHOD
         * ===================================================
         */

        else if (current.getType().name().equals("METHOD")) {

            /*
             * ------------------------------------------------
             * Find methods that call this method.
             *
             * Teacher.teach()
             *        ↑
             *        |
             *      CALLS
             *        |
             * Student.study()
             * ------------------------------------------------
             */

            addIncomingNodes(
                    current,
                    EdgeType.CALLS,
                    queue,
                    visited,
                    impactedNodes
            );


            /*
             * ------------------------------------------------
             * Find the class containing this method.
             *
             * Student
             *    |
             *    | HAS_METHOD
             *    v
             * Student.study()
             *
             * We traverse backwards.
             * ------------------------------------------------
             */

            addIncomingNodes(
                    current,
                    EdgeType.HAS_METHOD,
                    queue,
                    visited,
                    impactedNodes
            );

        }

    }


    return impactedNodes;

}


    /*
     * -------------------------------------------------------
     * Standard BFS Impact Analysis
     * -------------------------------------------------------
     */

    private List<GraphNode> bfsImpact(
            String nodeId,
            Set<EdgeType> relationshipTypes) {

        List<GraphNode> impactedNodes =
                new ArrayList<>();


        if (relationshipTypes == null
                || relationshipTypes.isEmpty()) {

            return impactedNodes;

        }


        GraphNode startNode =
                query.findNodeById(nodeId);

        if (startNode == null) {

            return impactedNodes;

        }


        Queue<GraphNode> queue =
                new LinkedList<>();

        Set<String> visited =
                new LinkedHashSet<>();


        queue.add(startNode);

        visited.add(
                startNode.getId()
        );


        while (!queue.isEmpty()) {

            GraphNode current =
                    queue.poll();


            for (EdgeType type :
                    relationshipTypes) {

                addIncomingNodes(
                        current,
                        type,
                        queue,
                        visited,
                        impactedNodes
                );

            }

        }


        return impactedNodes;

    }


    /*
     * -------------------------------------------------------
     * Add Incoming Nodes
     * -------------------------------------------------------
     */

    private void addIncomingNodes(
            GraphNode current,
            EdgeType type,
            Queue<GraphNode> queue,
            Set<String> visited,
            List<GraphNode> impactedNodes) {

        List<GraphNode> incomingNodes =
                query.getIncomingNodes(
                        current.getId(),
                        type
                );


        for (GraphNode node :
                incomingNodes) {

            if (visited.contains(
                    node.getId())) {

                continue;

            }


            visited.add(
                    node.getId()
            );


            impactedNodes.add(
                    node
            );


            queue.add(
                    node
            );

        }

    }


    /*
     * -------------------------------------------------------
     * Add Outgoing Nodes
     * -------------------------------------------------------
     */

    private void addOutgoingNodes(
            GraphNode current,
            EdgeType type,
            Queue<GraphNode> queue,
            Set<String> visited,
            List<GraphNode> impactedNodes) {

        List<GraphNode> outgoingNodes =
                query.getOutgoingNodes(
                        current.getId(),
                        type
                );


        for (GraphNode node :
                outgoingNodes) {

            if (visited.contains(
                    node.getId())) {

                continue;

            }


            visited.add(
                    node.getId()
            );


            impactedNodes.add(
                    node
            );


            queue.add(
                    node
            );

        }

    }

}