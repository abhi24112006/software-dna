package com.softwaredna.parser.nlp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.NodeType;

class QueryPlannerTest {

    private final QueryPlanner planner = new QueryPlanner();

    private final GraphNode userController =
            new GraphNode(
                    "class:userController",
                    "UserController",
                    NodeType.CLASS
            );

    @Test
    void shouldPlanDependenciesQuery() {

        QueryPlan plan =
                planner.plan(
                        QueryIntent.DEPENDENCIES,
                        userController
                );

        assertEquals(QueryIntent.DEPENDENCIES, plan.getIntent());
        assertEquals(userController, plan.getEntity());
        assertEquals(
                QueryOperation.GET_DEPENDENCIES,
                plan.getOperation()
        );
    }

    @Test
    void shouldPlanDependentsQuery() {

        QueryPlan plan =
                planner.plan(
                        QueryIntent.DEPENDENTS,
                        userController
                );

        assertEquals(
                QueryOperation.GET_DEPENDENTS,
                plan.getOperation()
        );
    }

    @Test
    void shouldPlanCalleesQuery() {

        QueryPlan plan =
                planner.plan(
                        QueryIntent.CALLEES,
                        userController
                );

        assertEquals(
                QueryOperation.GET_CALLEES,
                plan.getOperation()
        );
    }

    @Test
    void shouldPlanCallersQuery() {

        QueryPlan plan =
                planner.plan(
                        QueryIntent.CALLERS,
                        userController
                );

        assertEquals(
                QueryOperation.GET_CALLERS,
                plan.getOperation()
        );
    }

    @Test
    void shouldPlanSubclassesQuery() {

        QueryPlan plan =
                planner.plan(
                        QueryIntent.SUBCLASSES,
                        userController
                );

        assertEquals(
                QueryOperation.GET_SUBCLASSES,
                plan.getOperation()
        );
    }

    @Test
    void shouldPlanSuperclassQuery() {

        QueryPlan plan =
                planner.plan(
                        QueryIntent.SUPERCLASS,
                        userController
                );

        assertEquals(
                QueryOperation.GET_SUPERCLASS,
                plan.getOperation()
        );
    }

    @Test
    void shouldPlanImplementedInterfacesQuery() {

        QueryPlan plan =
                planner.plan(
                        QueryIntent.IMPLEMENTED_INTERFACES,
                        userController
                );

        assertEquals(
                QueryOperation.GET_IMPLEMENTED_INTERFACES,
                plan.getOperation()
        );
    }

    @Test
    void shouldPlanImplementationsQuery() {

        QueryPlan plan =
                planner.plan(
                        QueryIntent.IMPLEMENTATIONS,
                        userController
                );

        assertEquals(
                QueryOperation.GET_IMPLEMENTATIONS,
                plan.getOperation()
        );
    }

    @Test
    void shouldPlanImpactQuery() {

        QueryPlan plan =
                planner.plan(
                        QueryIntent.IMPACT,
                        userController
                );

        assertEquals(
                QueryOperation.GET_IMPACT,
                plan.getOperation()
        );
    }

    @Test
    void shouldPlanReachabilityQuery() {

        QueryPlan plan =
                planner.plan(
                        QueryIntent.REACHABILITY,
                        userController
                );

        assertEquals(
                QueryOperation.GET_REACHABILITY,
                plan.getOperation()
        );
    }

    @Test
    void shouldPlanArchitectureQuery() {

        QueryPlan plan =
                planner.plan(
                        QueryIntent.ARCHITECTURE,
                        userController
                );

        assertEquals(
                QueryOperation.GET_ARCHITECTURE,
                plan.getOperation()
        );
    }

    @Test
    void shouldRejectUnknownIntent() {

        assertThrows(
                IllegalArgumentException.class,
                () -> planner.plan(
                        QueryIntent.UNKNOWN,
                        userController
                )
        );
    }

    @Test
    void shouldRejectNullIntent() {

        assertThrows(
                IllegalArgumentException.class,
                () -> planner.plan(
                        null,
                        userController
                )
        );
    }

    @Test
    void shouldRejectNullEntity() {

        assertThrows(
                IllegalArgumentException.class,
                () -> planner.plan(
                        QueryIntent.DEPENDENCIES,
                        null
                )
        );
    }
}