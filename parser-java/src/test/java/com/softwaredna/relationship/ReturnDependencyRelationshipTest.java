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

class ReturnDependencyRelationshipTest {

    @Test
    void shouldAttachSourceEvidenceToReturnDependency()
            throws Exception {

        Path tempDirectory =
                Files.createTempDirectory(
                        "software-dna-return-test"
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
                    public Person getPerson() {
                        return null;
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


        Relationship returnDependency =
                repository.getRelationships()
                        .stream()
                        .filter(r ->
                                r.getType()
                                        == RelationshipType.RETURN_DEPENDENCY)
                        .findFirst()
                        .orElse(null);


        assertNotNull(
                returnDependency,
                "RETURN_DEPENDENCY relationship should exist"
        );


        assertNotNull(
                returnDependency.getSourceEvidence(),
                "RETURN_DEPENDENCY should have source evidence"
        );


        assertEquals(
                studentFile.toString(),
                returnDependency
                        .getSourceEvidence()
                        .getFilePath()
        );


        assertEquals(
                2,
                returnDependency
                        .getSourceEvidence()
                        .getLineNumber()
        );
    }
}