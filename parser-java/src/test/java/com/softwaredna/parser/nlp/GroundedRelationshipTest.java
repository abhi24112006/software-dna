package com.softwaredna.parser.nlp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.softwaredna.knowledge.EdgeType;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.NodeType;

class GroundedRelationshipTest {

    @Test
    void storesSourceTargetAndType() {

        GraphNode source =
                new GraphNode(
                        "controller.UserController",
                        "UserController",
                        NodeType.CLASS);

        GraphNode target =
                new GraphNode(
                        "service.UserService",
                        "UserService",
                        NodeType.CLASS);

        GroundedRelationship relationship =
                new GroundedRelationship(
                        source,
                        target,
                        EdgeType.DEPENDS_ON);

        assertEquals(source, relationship.getSource());
        assertEquals(target, relationship.getTarget());
        assertEquals(
                EdgeType.DEPENDS_ON,
                relationship.getType());
    }

    @Test
    void rejectsNullSource() {

        GraphNode target =
                new GraphNode(
                        "service.UserService",
                        "UserService",
                        NodeType.CLASS);

        assertThrows(
                IllegalArgumentException.class,
                () -> new GroundedRelationship(
                        null,
                        target,
                        EdgeType.DEPENDS_ON));
    }

    @Test
    void rejectsNullTarget() {

        GraphNode source =
                new GraphNode(
                        "controller.UserController",
                        "UserController",
                        NodeType.CLASS);

        assertThrows(
                IllegalArgumentException.class,
                () -> new GroundedRelationship(
                        source,
                        null,
                        EdgeType.DEPENDS_ON));
    }

    @Test
    void rejectsNullType() {

        GraphNode source =
                new GraphNode(
                        "controller.UserController",
                        "UserController",
                        NodeType.CLASS);

        GraphNode target =
                new GraphNode(
                        "service.UserService",
                        "UserService",
                        NodeType.CLASS);

        assertThrows(
                IllegalArgumentException.class,
                () -> new GroundedRelationship(
                        source,
                        target,
                        null));
    }

    @Test
    void relationshipsWithSameIdentityAreEqual() {

        GraphNode source1 =
                new GraphNode(
                        "controller.UserController",
                        "UserController",
                        NodeType.CLASS);

        GraphNode target1 =
                new GraphNode(
                        "service.UserService",
                        "UserService",
                        NodeType.CLASS);

        GraphNode source2 =
                new GraphNode(
                        "controller.UserController",
                        "UserController",
                        NodeType.CLASS);

        GraphNode target2 =
                new GraphNode(
                        "service.UserService",
                        "UserService",
                        NodeType.CLASS);

        GroundedRelationship first =
                new GroundedRelationship(
                        source1,
                        target1,
                        EdgeType.DEPENDS_ON);

        GroundedRelationship second =
                new GroundedRelationship(
                        source2,
                        target2,
                        EdgeType.DEPENDS_ON);

        assertEquals(first, second);
        assertEquals(
                first.hashCode(),
                second.hashCode());
    }
}