package com.softwaredna.knowledge.query;

import java.util.ArrayList;
import java.util.List;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;

public class KnowledgeGraphQuery {

    private final KnowledgeGraph graph;

    public KnowledgeGraphQuery(
            KnowledgeGraph graph) {

        if (graph == null) {
            throw new IllegalArgumentException(
                    "KnowledgeGraph cannot be null."
            );
        }

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

        for (GraphEdge edge : graph.getEdges()) {

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

        for (GraphEdge edge : graph.getEdges()) {

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

        for (GraphEdge edge : graph.getEdges()) {

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

        for (GraphEdge edge : graph.getEdges()) {

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

            result.add(edge.getTarget());
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

            result.add(edge.getSource());
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
     * Methods Called By A Class
     *
     * Traversal:
     *
     * Class
     *   |
     *   | HAS_METHOD
     *   v
     * Method
     *   |
     *   | CALLS
     *   v
     * Called Method
     *
     * Example:
     *
     * UserController
     *       |
     *       | HAS_METHOD
     *       v
     * UserController.create()
     *       |
     *       | CALLS
     *       v
     * UserService.create_user()
     * -------------------------------------------------------
     */

    public List<GraphNode> getClassCallees(
            String classId) {

        List<GraphNode> result =
                new ArrayList<>();

        List<GraphNode> methods =
                getOutgoingNodes(
                        classId,
                        EdgeType.HAS_METHOD
                );

        for (GraphNode method : methods) {

            List<GraphNode> callees =
                    getCallees(
                            method.getId()
                    );

            for (GraphNode callee : callees) {

                if (!result.contains(callee)) {
                    result.add(callee);
                }
            }
        }

        return result;
    }

    /*
     * -------------------------------------------------------
     * Classes Calling A Class
     *
     * Traversal:
     *
     * Calling Class
     *       |
     *       | HAS_METHOD
     *       v
     * Calling Method
     *       |
     *       | CALLS
     *       v
     * Target Method
     *       ^
     *       |
     *       | HAS_METHOD
     *       |
     * Target Class
     * -------------------------------------------------------
     */

    public List<GraphNode> getClassCallers(
            String classId) {

        List<GraphNode> result =
                new ArrayList<>();

        List<GraphNode> targetMethods =
                getOutgoingNodes(
                        classId,
                        EdgeType.HAS_METHOD
                );

        for (GraphNode targetMethod : targetMethods) {

            List<GraphNode> callers =
                    getCallers(
                            targetMethod.getId()
                    );

            for (GraphNode callerMethod : callers) {

                GraphNode callerClass =
                        findContainingClass(
                                callerMethod
                        );

                if (callerClass != null
                        && !result.contains(callerClass)) {

                    result.add(callerClass);
                }
            }
        }

        return result;
    }

    /*
     * -------------------------------------------------------
     * Find Containing Class
     * -------------------------------------------------------
     */

    private GraphNode findContainingClass(
            GraphNode method) {

        if (method == null) {
            return null;
        }

        for (GraphEdge edge : graph.getEdges()) {

            if (edge.getType() != EdgeType.HAS_METHOD) {
                continue;
            }

            if (!edge.getTarget()
                    .getId()
                    .equals(method.getId())) {
                continue;
            }

            GraphNode source = edge.getSource();

            if (source.getType() == NodeType.CLASS) {
                return source;
            }
        }

        return null;
    }

    /*
     * -------------------------------------------------------
     * Subclasses
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