package com.softwaredna.type;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TypeReferenceExtractor {

    private static final Set<String> IGNORED_TYPES = Set.of(

            // Primitive Types
            "byte",
            "short",
            "int",
            "long",
            "float",
            "double",
            "boolean",
            "char",
            "void",

            // Common Java Types
            "String",
            "Object",

            // Collection Types
            "List",
            "ArrayList",
            "LinkedList",
            "Set",
            "HashSet",
            "TreeSet",
            "Map",
            "HashMap",
            "TreeMap",
            "Queue",
            "Deque",

            // Wrapper Types
            "Integer",
            "Long",
            "Double",
            "Float",
            "Boolean",
            "Character",
            "Byte",
            "Short",

            // Utility Types
            "Optional"

    );

    public List<String> extractReferencedTypes(String type) {

        if (type == null || type.isBlank()) {

            return List.of();

        }

        Set<String> referencedTypes = new LinkedHashSet<>();

        String normalizedType = normalize(type);

        String[] tokens = normalizedType.split("\\s+");

        for (String token : tokens) {

            if (isProjectType(token)) {

                referencedTypes.add(token);

            }

        }

        return new ArrayList<>(referencedTypes);

    }

    private String normalize(String type) {

        return type

                .replace("<", " ")
                .replace(">", " ")
                .replace(",", " ")
                .replace("[", " ")
                .replace("]", " ")
                .replace("?", " ")
                .replace("&", " ");

    }

    private boolean isProjectType(String token) {

        if (token.isBlank()) {

            return false;

        }

        return !IGNORED_TYPES.contains(token);

    }

}