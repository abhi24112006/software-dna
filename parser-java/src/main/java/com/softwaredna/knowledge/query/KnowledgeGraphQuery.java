package com.softwaredna.knowledge.query;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeGraphQuery {

    private final KnowledgeGraph graph;

    public KnowledgeGraphQuery(
            KnowledgeGraph graph) {

        this.graph = graph;

    }

    /*
     * -------------------------------------------------------
     * Find Node
     * -------------------------------------------------------
     */

    public GraphNode findNodeById(
            String nodeId) {

        return graph.getNode(nodeId);

    }


    /*
     * -------------------------------------------------------
     * Outgoing Edges
     *
     * Example:
     *
     * StudentService
     *      |
     *      | DEPENDS_ON
     *      v
     * Student
     * -------------------------------------------------------
     */

    public List<GraphEdge> getOutgoingEdges(
            String nodeId) {

        List<GraphEdge> result =
                new ArrayList<>();

        for (GraphEdge edge :
                graph.getEdges()) {

            if (edge.getSource()
                    .getId()
                    .equals(nodeId)) {

                result.add(edge);

            }

        }

        return result;

    }


    /*
     * -------------------------------------------------------
     * Incoming Edges
     * -------------------------------------------------------
     */

    public List<GraphEdge> getIncomingEdges(
            String nodeId) {

        List<GraphEdge> result =
                new ArrayList<>();

        for (GraphEdge edge :
                graph.getEdges()) {

            if (edge.getTarget()
                    .getId()
                    .equals(nodeId)) {

                result.add(edge);

            }

        }

        return result;

    }


    /*
     * -------------------------------------------------------
     * Outgoing Edges By Type
     * -------------------------------------------------------
     */

    public List<GraphEdge> getOutgoingEdges(
            String nodeId,
            EdgeType type) {

        List<GraphEdge> result =
                new ArrayList<>();

        for (GraphEdge edge :
                graph.getEdges()) {

            if (edge.getSource()
                    .getId()
                    .equals(nodeId)
                    && edge.getType() == type) {

                result.add(edge);

            }

        }

        return result;

    }


    /*
     * -------------------------------------------------------
     * Incoming Edges By Type
     * -------------------------------------------------------
     */

    public List<GraphEdge> getIncomingEdges(
            String nodeId,
            EdgeType type) {

        List<GraphEdge> result =
                new ArrayList<>();

        for (GraphEdge edge :
                graph.getEdges()) {

            if (edge.getTarget()
                    .getId()
                    .equals(nodeId)
                    && edge.getType() == type) {

                result.add(edge);

            }

        }

        return result;

    }


    /*
     * -------------------------------------------------------
     * Outgoing Connected Nodes
     * -------------------------------------------------------
     */

    public List<GraphNode> getOutgoingNodes(
            String nodeId,
            EdgeType type) {

        List<GraphNode> result =
                new ArrayList<>();

        for (GraphEdge edge :
                getOutgoingEdges(nodeId, type)) {

            result.add(
                    edge.getTarget()
            );

        }

        return result;

    }


    /*
     * -------------------------------------------------------
     * Incoming Connected Nodes
     * -------------------------------------------------------
     */

    public List<GraphNode> getIncomingNodes(
            String nodeId,
            EdgeType type) {

        List<GraphNode> result =
                new ArrayList<>();

        for (GraphEdge edge :
                getIncomingEdges(nodeId, type)) {

            result.add(
                    edge.getSource()
            );

        }

        return result;

    }

}