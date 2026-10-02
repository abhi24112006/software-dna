package com.softwaredna.graph;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.softwaredna.model.Relationship;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.parser.RepositoryParser;

class GraphIntegrityTest {

    @Test
    void shouldEnsureAllRelationshipEndpointsAreValid()
            throws Exception {

        Path tempDir =
                Files.createTempDirectory(
                        "graph-integrity-test");

        Files.writeString(
                tempDir.resolve("Teacher.java"),
                """
                package demo;

                public class Teacher {

                    public void teach() {
                    }
                }
                """);

        Files.writeString(
                tempDir.resolve("Student.java"),
                """
                package demo;

                public class Student {

                    private Teacher teacher;

                    public void study() {
                        teacher.teach();
                    }
                }
                """);

        RepositoryParser parser =
                new RepositoryParser();

        RepositoryModel repository =
                parser.parseRepository(
                        tempDir.toString());

        Set<String> registeredEntityIds =
                new HashSet<>();

        for (Object entity :
                repository.getEntityRegistry()
                        .getAllParsedEntities()) {

            if (entity instanceof
                    com.softwaredna.model.ParsedClass parsedClass) {

                registeredEntityIds.add(
                        parsedClass.getId());

                parsedClass.getFields()
                        .forEach(field ->
                                registeredEntityIds.add(
                                        field.getId()));

                parsedClass.getConstructors()
                        .forEach(constructor ->
                                registeredEntityIds.add(
                                        constructor.getId()));

                parsedClass.getMethods()
                        .forEach(method -> {
                            registeredEntityIds.add(
                                    method.getId());

                            method.getParameters()
                                    .forEach(parameter ->
                                            registeredEntityIds.add(
                                                    parameter.getId()));
                        });

            } else if (entity instanceof
                    com.softwaredna.model.ParsedInterface parsedInterface) {

                registeredEntityIds.add(
                        parsedInterface.getId());

            } else if (entity instanceof
                    com.softwaredna.model.ParsedEnum parsedEnum) {

                registeredEntityIds.add(
                        parsedEnum.getId());

            } else if (entity instanceof
                    com.softwaredna.model.ParsedRecord parsedRecord) {

                registeredEntityIds.add(
                        parsedRecord.getId());

            } else if (entity instanceof
                    com.softwaredna.model.ParsedField parsedField) {

                registeredEntityIds.add(
                        parsedField.getId());

            } else if (entity instanceof
                    com.softwaredna.model.ParsedMethod parsedMethod) {

                registeredEntityIds.add(
                        parsedMethod.getId());

            } else if (entity instanceof
                    com.softwaredna.model.ParsedConstructor parsedConstructor) {

                registeredEntityIds.add(
                        parsedConstructor.getId());

            } else if (entity instanceof
                    com.softwaredna.model.ParsedParameter parsedParameter) {

                registeredEntityIds.add(
                        parsedParameter.getId());
            }
        }

        for (Relationship relationship :
                repository.getRelationships()) {

            assertNotNull(
                    relationship.getSource(),
                    "Relationship source must not be null");

            assertNotNull(
                    relationship.getTarget(),
                    "Relationship target must not be null");

            assertNotNull(
                    relationship.getSource().getId(),
                    "Relationship source ID must not be null");

            assertNotNull(
                    relationship.getTarget().getId(),
                    "Relationship target ID must not be null");

            assertNotNull(
                    relationship.getType(),
                    "Relationship type must not be null");

            assertTrue(
                    registeredEntityIds.contains(
                            relationship.getSource().getId()),
                    "Relationship source must refer to a registered parsed entity");

            assertTrue(
                    registeredEntityIds.contains(
                            relationship.getTarget().getId()),
                    "Relationship target must refer to a registered parsed entity");
        }
    }

    @Test
    void shouldContainNoDuplicateRelationships()
            throws Exception {

        Path tempDir =
                Files.createTempDirectory(
                        "graph-duplicate-test");

        Files.writeString(
                tempDir.resolve("Teacher.java"),
                """
                package demo;

                public class Teacher {

                    public void teach() {
                    }
                }
                """);

        Files.writeString(
                tempDir.resolve("Student.java"),
                """
                package demo;

                public class Student {

                    private Teacher teacher;

                    public void study() {
                        teacher.teach();
                        teacher.teach();
                    }
                }
                """);

        RepositoryParser parser =
                new RepositoryParser();

        RepositoryModel repository =
                parser.parseRepository(
                        tempDir.toString());

        Set<Relationship> uniqueRelationships =
                new HashSet<>(
                        repository.getRelationships());

        assertTrue(
                uniqueRelationships.size()
                        == repository.getRelationships().size(),
                "Repository must not contain duplicate relationships");
    }
}