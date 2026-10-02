package com.softwaredna.identifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.softwaredna.model.ParsedEnum;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.ParsedRecord;
import com.softwaredna.model.RepositoryModel;

class IdentifierAssignerTest {

    @Test
    void assignsIdsToEnumsAndRecords() {

        RepositoryModel repository =
                new RepositoryModel();

        ParsedFile file =
                new ParsedFile();

        file.setPackageName(
                "com.example.domain"
        );

        ParsedEnum parsedEnum =
                new ParsedEnum("UserRole");

        ParsedRecord parsedRecord =
                new ParsedRecord("UserDto");

        file.getEnums().add(parsedEnum);
        file.getRecords().add(parsedRecord);

        repository.getFiles().add(file);

        IdentifierAssigner identifierAssigner =
                new IdentifierAssigner();

        identifierAssigner.assignIds(
                repository
        );

        assertNotNull(
                parsedEnum.getId()
        );

        assertNotNull(
                parsedRecord.getId()
        );

        assertEquals(
                "com.example.domain.UserRole",
                parsedEnum.getId()
        );

        assertEquals(
                "com.example.domain.UserDto",
                parsedRecord.getId()
        );
    }
}