package com.softwaredna.registry;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.softwaredna.model.ParsedClass;

class EntityRegistryDuplicateTest {

    @Test
    void shouldRejectDuplicateEntityId() {

        EntityRegistry registry =
                new EntityRegistry();

        ParsedClass first =
                new ParsedClass("User");

        first.setId("com.example.User");

        ParsedClass second =
                new ParsedClass("Account");

        second.setId("com.example.User");

        registry.registerClass(first);

        assertThrows(
                IllegalArgumentException.class,
                () -> registry.registerClass(second)
        );
    }


    @Test
    void shouldAllowRegisteringSameEntityAgain() {

        EntityRegistry registry =
                new EntityRegistry();

        ParsedClass parsedClass =
                new ParsedClass("User");

        parsedClass.setId("com.example.User");

        registry.registerClass(parsedClass);

        assertDoesNotThrow(
                () -> registry.registerClass(parsedClass)
        );
    }
}