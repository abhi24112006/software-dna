package com.softwaredna.relationship;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.softwaredna.model.Relationship;
import com.softwaredna.model.RelationshipType;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.parser.RepositoryParser;

class ParameterDependencyRelationshipTest {

    @Test
    void shouldAttachSourceEvidenceToParameterDependency()
            throws Exception {

        Path tempDirectory =
                Files.createTempDirectory(
                        "software-dna-parameter-test"
                );

        Path personFile =
                tempDirectory.resolve("Person.java");

        Path studentFile =
                tempDirectory.resolve("Student.java");


        Files.writeString(
                personFile,
                """
                public class Person {
                }
                """
        );


        Files.writeString(
                studentFile,
                """
                public class Student {
                    public void enroll(Person person) {
                    }
                }
                """
        );


        RepositoryParser parser =
                new RepositoryParser();

        RepositoryModel repository =
                parser.parseRepository(
                        tempDirectory.toString()
                );


        Relationship parameterDependency =
                repository.getRelationships()
                        .stream()
                        .filter(r ->
                                r.getType()
                                        == RelationshipType.PARAMETER_DEPENDENCY)
                        .findFirst()
                        .orElse(null);


        assertNotNull(
                parameterDependency,
                "PARAMETER_DEPENDENCY relationship should exist"
        );


        assertNotNull(
                parameterDependency.getSourceEvidence(),
                "PARAMETER_DEPENDENCY should have source evidence"
        );


        assertEquals(
                studentFile.toString(),
                parameterDependency
                        .getSourceEvidence()
                        .getFilePath()
        );


        assertEquals(
                2,
                parameterDependency
                        .getSourceEvidence()
                        .getLineNumber()
        );
    }
}