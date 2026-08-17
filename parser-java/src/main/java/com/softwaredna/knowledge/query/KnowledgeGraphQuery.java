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
                getOutgoingEdges(
                        nodeId,
                        type)) {

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
                getIncomingEdges(
                        nodeId,
                        type)) {

            result.add(
                    edge.getSource()
            );

        }

        return result;

    }


    /*
     * =======================================================
     * SEMANTIC QUERIES
     * =======================================================
     */


    /*
     * -------------------------------------------------------
     * Dependencies
     *
     * Example:
     *
     * StudentService
     *       |
     *       | DEPENDS_ON
     *       v
     *    Student
     * -------------------------------------------------------
     */

    public List<GraphNode> getDependencies(
            String nodeId) {

        return getOutgoingNodes(
                nodeId,
                EdgeType.DEPENDS_ON
        );

    }


    /*
     * -------------------------------------------------------
     * Dependents
     *
     * Reverse of DEPENDS_ON.
     *
     * Example:
     *
     * StudentService
     *       |
     *       | DEPENDS_ON
     *       v
     *    Student
     *
     * getDependents(Student)
     *
     * -> StudentService
     * -------------------------------------------------------
     */

    public List<GraphNode> getDependents(
            String nodeId) {

        return getIncomingNodes(
                nodeId,
                EdgeType.DEPENDS_ON
        );

    }


    /*
     * -------------------------------------------------------
     * Methods Called By A Method
     *
     * Example:
     *
     * Student.study()
     *       |
     *       | CALLS
     *       v
     * Teacher.teach()
     * -------------------------------------------------------
     */

    public List<GraphNode> getCallees(
            String methodId) {

        return getOutgoingNodes(
                methodId,
                EdgeType.CALLS
        );

    }


    /*
     * -------------------------------------------------------
     * Methods Calling A Method
     *
     * Reverse of CALLS.
     *
     * Example:
     *
     * Student.study()
     *       |
     *       | CALLS
     *       v
     * Teacher.teach()
     *
     * getCallers(Teacher.teach())
     *
     * -> Student.study()
     * -------------------------------------------------------
     */

    public List<GraphNode> getCallers(
            String methodId) {

        return getIncomingNodes(
                methodId,
                EdgeType.CALLS
        );

    }


    /*
     * -------------------------------------------------------
     * Subclasses
     *
     * Example:
     *
     * Mammal ---- EXTENDS ----> Animal
     *
     * getSubclasses(Animal)
     *
     * -> Mammal
     * -------------------------------------------------------
     */

    public List<GraphNode> getSubclasses(
            String classId) {

        return getIncomingNodes(
                classId,
                EdgeType.EXTENDS
        );

    }


    /*
     * -------------------------------------------------------
     * Superclass
     *
     * Example:
     *
     * Mammal ---- EXTENDS ----> Animal
     *
     * getSuperclass(Mammal)
     *
     * -> Animal
     * -------------------------------------------------------
     */

    public List<GraphNode> getSuperclass(
            String classId) {

        return getOutgoingNodes(
                classId,
                EdgeType.EXTENDS
        );

    }


    /*
     * -------------------------------------------------------
     * Implemented Interfaces
     *
     * Example:
     *
     * Report ---- IMPLEMENTS ----> Printable
     *
     * getImplementedInterfaces(Report)
     *
     * -> Printable
     * -------------------------------------------------------
     */

    public List<GraphNode> getImplementedInterfaces(
            String classId) {

        return getOutgoingNodes(
                classId,
                EdgeType.IMPLEMENTS
        );

    }


    /*
     * -------------------------------------------------------
     * Implementing Classes
     *
     * Reverse of IMPLEMENTS.
     *
     * Example:
     *
     * Report ---- IMPLEMENTS ----> Printable
     *
     * getImplementations(Printable)
     *
     * -> Report
     * -------------------------------------------------------
     */

    public List<GraphNode> getImplementations(
            String interfaceId) {

        return getIncomingNodes(
                interfaceId,
                EdgeType.IMPLEMENTS
        );

    }

}