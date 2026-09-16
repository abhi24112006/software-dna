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

class ExtendsRelationshipTest {

    @Test
    void shouldAttachSourceEvidenceToExtendsRelationship()
            throws Exception {

        Path tempDirectory =
                Files.createTempDirectory("software-dna-extends-test");

        Path parentFile =
                tempDirectory.resolve("Person.java");

        Path childFile =
                tempDirectory.resolve("Student.java");


        Files.writeString(
                parentFile,
                """
                public class Person {
                }
                """
        );


        Files.writeString(
                childFile,
                """
                public class Student extends Person {
                    private String name;
                }
                """
        );


        RepositoryParser parser =
                new RepositoryParser();

        RepositoryModel repository =
                parser.parseRepository(
                        tempDirectory.toString()
                );


        Relationship extendsRelationship =
                repository.getRelationships()
                        .stream()
                        .filter(r ->
                                r.getType()
                                        == RelationshipType.EXTENDS)
                        .findFirst()
                        .orElse(null);


        assertNotNull(
                extendsRelationship,
                "EXTENDS relationship should exist"
        );


        assertNotNull(
                extendsRelationship.getSourceEvidence(),
                "EXTENDS relationship should have source evidence"
        );


        assertEquals(
                childFile.toString(),
                extendsRelationship
                        .getSourceEvidence()
                        .getFilePath()
        );


        assertEquals(
                1,
                extendsRelationship
                        .getSourceEvidence()
                        .getLineNumber()
        );
    }
}