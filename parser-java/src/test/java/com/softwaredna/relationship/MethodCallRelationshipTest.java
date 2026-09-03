package com.softwaredna.relationship;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

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
}