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

class FieldDependencyRelationshipTest {

    @Test
    void shouldAttachSourceEvidenceToFieldDependency()
            throws Exception {

        Path tempDirectory =
                Files.createTempDirectory(
                        "software-dna-field-test"
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

                    private Person person;
                }
                """
        );


        RepositoryParser parser =
                new RepositoryParser();

        RepositoryModel repository =
                parser.parseRepository(
                        tempDirectory.toString()
                );


        Relationship fieldDependency =
                repository.getRelationships()
                        .stream()
                        .filter(r ->
                                r.getType()
                                        == RelationshipType.FIELD_DEPENDENCY)
                        .findFirst()
                        .orElse(null);


        assertNotNull(
                fieldDependency,
                "FIELD_DEPENDENCY relationship should exist"
        );


        assertNotNull(
                fieldDependency.getSourceEvidence(),
                "FIELD_DEPENDENCY should have source evidence"
        );


        assertEquals(
                studentFile.toString(),
                fieldDependency
                        .getSourceEvidence()
                        .getFilePath()
        );


        assertEquals(
                3,
                fieldDependency
                        .getSourceEvidence()
                        .getLineNumber()
        );
    }
}