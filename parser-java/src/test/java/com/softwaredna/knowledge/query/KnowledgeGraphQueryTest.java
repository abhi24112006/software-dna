package com.softwaredna.knowledge.query;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KnowledgeGraphQueryTest {

    private KnowledgeGraph graph;
    private KnowledgeGraphQuery query;

    private GraphNode controller;
    private GraphNode controllerCreate;

    private GraphNode service;
    private GraphNode serviceCreate;

    private GraphNode repository;
    private GraphNode repositorySave;

    @BeforeEach
    void setUp() {

        graph = new KnowledgeGraph();

        controller =
                new GraphNode(
                        "class:UserController",
                        "UserController",
                        NodeType.CLASS
                );

        controllerCreate =
                new GraphNode(
                        "method:UserController.create",
                        "UserController.create()",
                        NodeType.METHOD
                );

        service =
                new GraphNode(
                        "class:UserService",
                        "UserService",
                        NodeType.CLASS
                );

        serviceCreate =
                new GraphNode(
                        "method:UserService.create_user",
                        "UserService.create_user()",
                        NodeType.METHOD
                );

        repository =
                new GraphNode(
                        "class:UserRepository",
                        "UserRepository",
                        NodeType.CLASS
                );

        repositorySave =
                new GraphNode(
                        "method:UserRepository.save",
                        "UserRepository.save()",
                        NodeType.METHOD
                );

        graph.addNode(controller);
        graph.addNode(controllerCreate);
        graph.addNode(service);
        graph.addNode(serviceCreate);
        graph.addNode(repository);
        graph.addNode(repositorySave);

        graph.addEdge(
                new GraphEdge(
                        controller,
                        controllerCreate,
                        EdgeType.HAS_METHOD
                )
        );

        graph.addEdge(
                new GraphEdge(
                        service,
                        serviceCreate,
                        EdgeType.HAS_METHOD
                )
        );

        graph.addEdge(
                new GraphEdge(
                        repository,
                        repositorySave,
                        EdgeType.HAS_METHOD
                )
        );

        graph.addEdge(
                new GraphEdge(
                        controllerCreate,
                        serviceCreate,
                        EdgeType.CALLS
                )
        );

        graph.addEdge(
                new GraphEdge(
                        serviceCreate,
                        repositorySave,
                        EdgeType.CALLS
                )
        );

        query =
                new KnowledgeGraphQuery(graph);
    }

    @Test
    void shouldGetCalleesOfMethod() {

        List<GraphNode> result =
                query.getCallees(
                        controllerCreate.getId()
                );

        assertEquals(1, result.size());

        assertEquals(
                "UserService.create_user()",
                result.get(0).getName()
        );
    }

    @Test
    void shouldGetCalleesOfClass() {

        List<GraphNode> result =
                query.getClassCallees(
                        controller.getId()
                );

        assertEquals(1, result.size());

        assertEquals(
                "UserService.create_user()",
                result.get(0).getName()
        );
    }

    @Test
    void shouldGetAllCalleesAcrossMultipleMethods() {

        GraphNode secondMethod =
                new GraphNode(
                        "method:UserController.update",
                        "UserController.update()",
                        NodeType.METHOD
                );

        GraphNode repositorySave2 =
                new GraphNode(
                        "method:UserRepository.delete",
                        "UserRepository.delete()",
                        NodeType.METHOD
                );

        graph.addNode(secondMethod);
        graph.addNode(repositorySave2);

        graph.addEdge(
                new GraphEdge(
                        controller,
                        secondMethod,
                        EdgeType.HAS_METHOD
                )
        );

        graph.addEdge(
                new GraphEdge(
                        secondMethod,
                        repositorySave2,
                        EdgeType.CALLS
                )
        );

        List<GraphNode> result =
                query.getClassCallees(
                        controller.getId()
                );

        assertEquals(2, result.size());

        assertTrue(
                result.contains(serviceCreate)
        );

        assertTrue(
                result.contains(repositorySave2)
        );
    }

    @Test
    void shouldNotDuplicateCalleeReachedByMultipleMethods() {

        GraphNode secondMethod =
                new GraphNode(
                        "method:UserController.update",
                        "UserController.update()",
                        NodeType.METHOD
                );

        graph.addNode(secondMethod);

        graph.addEdge(
                new GraphEdge(
                        controller,
                        secondMethod,
                        EdgeType.HAS_METHOD
                )
        );

        graph.addEdge(
                new GraphEdge(
                        secondMethod,
                        serviceCreate,
                        EdgeType.CALLS
                )
        );

        List<GraphNode> result =
                query.getClassCallees(
                        controller.getId()
                );

        assertEquals(1, result.size());

        assertEquals(
                "UserService.create_user()",
                result.get(0).getName()
        );
    }

    @Test
    void shouldGetClassesCallingTargetClass() {

        List<GraphNode> result =
                query.getClassCallers(
                        service.getId()
                );

        assertEquals(1, result.size());

        assertEquals(
                "UserController",
                result.get(0).getName()
        );
    }

    @Test
    void shouldGetNoClassCalleesWhenClassHasNoMethods() {

        GraphNode emptyClass =
                new GraphNode(
                        "class:EmptyClass",
                        "EmptyClass",
                        NodeType.CLASS
                );

        graph.addNode(emptyClass);

        List<GraphNode> result =
                query.getClassCallees(
                        emptyClass.getId()
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldPreserveExistingDependenciesQuery() {

        graph.addEdge(
                new GraphEdge(
                        controller,
                        service,
                        EdgeType.DEPENDS_ON
                )
        );

        List<GraphNode> result =
                query.getDependencies(
                        controller.getId()
                );

        assertEquals(1, result.size());

        assertEquals(
                "UserService",
                result.get(0).getName()
        );
    }
}