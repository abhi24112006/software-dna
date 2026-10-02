package com.softwaredna.relationship;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.softwaredna.model.Relationship;
import com.softwaredna.model.RelationshipType;
import com.softwaredna.parser.RepositoryParser;

class MethodCallRelationshipTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldCreateMethodCallRelationshipForFieldReceiver()
            throws Exception {

        Path studentFile =
                tempDir.resolve("Student.java");

        Files.writeString(
                studentFile,
                """
                class Student {
                    Teacher teacher = new Teacher();

                    void study() {
                        teacher.teach();
                    }
                }
                """
        );

        Path teacherFile =
                tempDir.resolve("Teacher.java");

        Files.writeString(
                teacherFile,
                """
                class Teacher {
                    void teach() {
                    }
                }
                """
        );

        RepositoryParser parser =
                new RepositoryParser();

        var repository =
                parser.parseRepository(
                        tempDir.toString()
                );

        Optional<Relationship> methodCall =
                repository.getRelationships()
                        .stream()
                        .filter(
                                relationship ->
                                        relationship.getType()
                                                == RelationshipType.METHOD_CALL_INTERNAL
                                                || relationship.getType()
                                                == RelationshipType.METHOD_CALL_EXTERNAL
                        )
                        .findFirst();

        assertTrue(
                methodCall.isPresent(),
                "Expected a method call relationship to be created"
        );
    }


    @Test
    void shouldResolveMethodCallsWhenMultipleClassesShareTheSameSimpleName()
            throws Exception {

        Files.createDirectories(
                tempDir.resolve("demo")
        );

        Files.writeString(
                tempDir.resolve("Student.java"),
                """
                package demo;

                class Student {
                    Teacher teacher = new Teacher();

                    void study() {
                        teacher.teach();
                    }
                }
                """
        );

        Files.writeString(
                tempDir.resolve("Teacher.java"),
                """
                package default;

                class Teacher {
                }
                """
        );

        Files.writeString(
                tempDir.resolve("demo/Teacher.java"),
                """
                package demo;

                public class Teacher {
                    public void teach() {
                    }
                }
                """
        );

        RepositoryParser parser =
                new RepositoryParser();

        var repository =
                parser.parseRepository(
                        tempDir.toString()
                );

        Optional<Relationship> methodCall =
                repository.getRelationships()
                        .stream()
                        .filter(
                                relationship ->
                                        relationship.getType()
                                                == RelationshipType.METHOD_CALL_INTERNAL
                                                || relationship.getType()
                                                == RelationshipType.METHOD_CALL_EXTERNAL
                        )
                        .findFirst();

        assertTrue(
                methodCall.isPresent(),
                "Expected a method call relationship to be created for a duplicate simple class name"
        );
    }


    @Test
    void shouldExtractMethodCallsFromMethodBodies()
            throws Exception {

        Path studentFile =
                tempDir.resolve("Student.java");

        Files.writeString(
                studentFile,
                """
                class Student {
                    Teacher teacher = new Teacher();

                    void study() {
                        teacher.teach();
                    }
                }
                """
        );

        Path teacherFile =
                tempDir.resolve("Teacher.java");

        Files.writeString(
                teacherFile,
                """
                class Teacher {
                    void teach() {
                    }
                }
                """
        );

        RepositoryParser parser =
                new RepositoryParser();

        var repository =
                parser.parseRepository(
                        tempDir.toString()
                );

        var studentClass =
                repository.getFiles()
                        .stream()
                        .flatMap(
                                file ->
                                        file.getClasses()
                                                .stream()
                        )
                        .filter(
                                parsedClass ->
                                        "Student".equals(
                                                parsedClass.getName()
                                        )
                        )
                        .findFirst()
                        .orElseThrow();

        var studyMethod =
                studentClass.getMethods()
                        .stream()
                        .filter(
                                method ->
                                        "study".equals(
                                                method.getName()
                                        )
                        )
                        .findFirst()
                        .orElseThrow();

        assertTrue(
                studyMethod.getAnalysisResult() != null,
                "Expected the study method to have an analysis result"
        );

        assertTrue(
                !studyMethod
                        .getAnalysisResult()
                        .getMethodCalls()
                        .isEmpty(),
                "Expected the study method to contain a parsed method call"
        );
    }


    @Test
    void shouldAttachSourceEvidenceToMethodCallRelationship()
            throws Exception {

        Path studentFile =
                tempDir.resolve("Student.java");

        Files.writeString(
                studentFile,
                """
                class Student {
                    Teacher teacher = new Teacher();

                    void study() {
                        teacher.teach();
                    }
                }
                """
        );

        Path teacherFile =
                tempDir.resolve("Teacher.java");

        Files.writeString(
                teacherFile,
                """
                class Teacher {
                    void teach() {
                    }
                }
                """
        );

        RepositoryParser parser =
                new RepositoryParser();

        var repository =
                parser.parseRepository(
                        tempDir.toString()
                );

        Relationship methodCall =
                repository.getRelationships()
                        .stream()
                        .filter(
                                relationship ->
                                        relationship.getType()
                                                == RelationshipType.METHOD_CALL_INTERNAL
                        )
                        .findFirst()
                        .orElseThrow(
                                () -> new AssertionError(
                                        "Expected an internal method call relationship"
                                )
                        );

        assertNotNull(
                methodCall.getSourceEvidence(),
                "Expected method call relationship to contain source evidence"
        );

        assertEquals(
                studentFile.toString(),
                methodCall.getSourceEvidence().getFilePath(),
                "Expected source evidence to point to Student.java"
        );

        assertEquals(
                5,
                methodCall.getSourceEvidence().getLineNumber(),
                "Expected source evidence to point to the method call line"
        );
    }

    @Test
void shouldCreateMethodCallRelationshipForImplicitReceiver()
        throws Exception {

    Path studentFile =
            tempDir.resolve("Student.java");

    Files.writeString(
            studentFile,
            """
            class Student {

                void study() {
                    revise();
                }

                void revise() {
                }
            }
            """
    );

    RepositoryParser parser =
            new RepositoryParser();

    var repository =
            parser.parseRepository(
                    tempDir.toString()
            );

    boolean relationshipExists =
            repository.getRelationships()
                    .stream()
                    .anyMatch(
                            relationship ->
                                    relationship.getType()
                                            == RelationshipType.METHOD_CALL_INTERNAL
                    );

    assertTrue(
            relationshipExists,
            "Expected an internal relationship for an implicit same-class method call"
    );
}

@Test
void shouldResolveOverloadedMethodByArgumentCount()
        throws Exception {

    Path callerFile =
            tempDir.resolve("Caller.java");

    Files.writeString(
            callerFile,
            """
            class Caller {

                Worker worker = new Worker();

                void run() {
                    worker.process("hello");
                }
            }
            """
    );

    Path workerFile =
            tempDir.resolve("Worker.java");

    Files.writeString(
            workerFile,
            """
            class Worker {

                void process() {
                }

                void process(String value) {
                }
            }
            """
    );

    RepositoryParser parser =
            new RepositoryParser();

    var repository =
            parser.parseRepository(
                    tempDir.toString()
            );

    var workerClass =
            repository.getFiles()
                    .stream()
                    .flatMap(
                            file ->
                                    file.getClasses()
                                            .stream()
                    )
                    .filter(
                            parsedClass ->
                                    "Worker".equals(
                                            parsedClass.getName()
                                    )
                    )
                    .findFirst()
                    .orElseThrow();

    var targetMethod =
            workerClass.getMethods()
                    .stream()
                    .filter(
                            method ->
                                    "process".equals(
                                            method.getName()
                                    )
                    )
                    .filter(
                            method ->
                                    method.getParameters()
                                            .size() == 1
                    )
                    .findFirst()
                    .orElseThrow();

    boolean relationshipExists =
            repository.getRelationships()
                    .stream()
                    .anyMatch(
                            relationship ->
                                    relationship.getType()
                                            == RelationshipType.METHOD_CALL_INTERNAL
                                            &&
                                    relationship.getTarget()
                                            .getId()
                                            .equals(
                                                    targetMethod.getId()
                                            )
                    );

    assertTrue(
            relationshipExists,
            "Expected the one-argument overload to be selected"
    );
}

@Test
void shouldNotCreateMethodCallRelationshipForUnknownMethod()
        throws Exception {

    Path callerFile =
            tempDir.resolve("Caller.java");

    Files.writeString(
            callerFile,
            """
            class Caller {

                Worker worker = new Worker();

                void run() {
                    worker.unknownMethod();
                }
            }
            """
    );

    Path workerFile =
            tempDir.resolve("Worker.java");

    Files.writeString(
            workerFile,
            """
            class Worker {

                void process() {
                }
            }
            """
    );

    RepositoryParser parser =
            new RepositoryParser();

    var repository =
            parser.parseRepository(
                    tempDir.toString()
            );

    boolean relationshipExists =
            repository.getRelationships()
                    .stream()
                    .anyMatch(
                            relationship ->
                                    relationship.getType()
                                            == RelationshipType.METHOD_CALL_INTERNAL
                                            ||
                                    relationship.getType()
                                            == RelationshipType.METHOD_CALL_EXTERNAL
                    );

    assertTrue(
            !relationshipExists,
            "Unknown method should not create a method call relationship"
    );
}
}