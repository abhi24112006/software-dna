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

class ImplementsRelationshipTest {

    @Test
    void shouldAttachSourceEvidenceToImplementsRelationship()
            throws Exception {

        Path tempDirectory =
                Files.createTempDirectory(
                        "software-dna-implements-test"
                );

        Path interfaceFile =
                tempDirectory.resolve("Person.java");

        Path classFile =
                tempDirectory.resolve("Student.java");


        Files.writeString(
                interfaceFile,
                """
                public interface Person {
                }
                """
        );


        Files.writeString(
                classFile,
                """
                public class Student implements Person {
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


        Relationship implementsRelationship =
                repository.getRelationships()
                        .stream()
                        .filter(r ->
                                r.getType()
                                        == RelationshipType.IMPLEMENTS)
                        .findFirst()
                        .orElse(null);


        assertNotNull(
                implementsRelationship,
                "IMPLEMENTS relationship should exist"
        );


        assertNotNull(
                implementsRelationship.getSourceEvidence(),
                "IMPLEMENTS relationship should have source evidence"
        );


        assertEquals(
                classFile.toString(),
                implementsRelationship
                        .getSourceEvidence()
                        .getFilePath()
        );


        assertEquals(
                1,
                implementsRelationship
                        .getSourceEvidence()
                        .getLineNumber()
        );
    }
}