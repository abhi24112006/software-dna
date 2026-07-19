package com.softwaredna.registry;

import com.softwaredna.model.*;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class EntityRegistry {

    /*
     * -------------------------------------------------------
     * Parsed Model Storage
     * -------------------------------------------------------
     */

    private final Map<String, Object> parsedEntitiesById =
            new LinkedHashMap<>();

    /*
     * -------------------------------------------------------
     * Graph Entity Storage
     * -------------------------------------------------------
     */

    private final Map<String, EntityReference> entityReferences =
            new LinkedHashMap<>();

    /*
     * -------------------------------------------------------
     * Lookup Maps
     * -------------------------------------------------------
     */

    private final Map<String, ParsedClass> classesByName =
            new LinkedHashMap<>();

    /*
     * -------------------------------------------------------
     * Registration Methods
     * -------------------------------------------------------
     */

    public void registerClass(ParsedClass parsedClass) {

        parsedEntitiesById.put(
                parsedClass.getId(),
                parsedClass
        );

        classesByName.put(
                parsedClass.getName(),
                parsedClass
        );

    }

    public void registerInterface(
            ParsedInterface parsedInterface) {

        parsedEntitiesById.put(
                parsedInterface.getId(),
                parsedInterface
        );

    }

    public void registerEnum(
            ParsedEnum parsedEnum) {

        parsedEntitiesById.put(
                parsedEnum.getId(),
                parsedEnum
        );

    }

    public void registerRecord(
            ParsedRecord parsedRecord) {

        parsedEntitiesById.put(
                parsedRecord.getId(),
                parsedRecord
        );

    }

    public void registerField(
            ParsedField parsedField) {

        parsedEntitiesById.put(
                parsedField.getId(),
                parsedField
        );

    }

    public void registerMethod(
            ParsedMethod parsedMethod) {

        parsedEntitiesById.put(
                parsedMethod.getId(),
                parsedMethod
        );

    }

    public void registerConstructor(
            ParsedConstructor parsedConstructor) {

        parsedEntitiesById.put(
                parsedConstructor.getId(),
                parsedConstructor
        );

    }

    public void registerParameter(
            ParsedParameter parsedParameter) {

        parsedEntitiesById.put(
                parsedParameter.getId(),
                parsedParameter
        );

    }

    /*
     * -------------------------------------------------------
     * Entity Reference Registration
     * -------------------------------------------------------
     */

    public void registerEntityReference(
            EntityReference entityReference) {

        entityReferences.put(
                entityReference.getId(),
                entityReference
        );

    }

    /*
     * -------------------------------------------------------
     * Lookup Methods
     * -------------------------------------------------------
     */

    public ParsedClass findClassByName(String name) {

        return classesByName.get(name);

    }

    public EntityReference findEntityReferenceById(String id) {

        return entityReferences.get(id);

    }

    public EntityReference findClassEntityByName(String className) {

        ParsedClass parsedClass = findClassByName(className);

        if (parsedClass == null) {
            return null;
        }

        return findEntityReferenceById(parsedClass.getId());

    }

    public Collection<Object> getAllParsedEntities() {

        return parsedEntitiesById.values();

    }

    public Collection<EntityReference> getAllEntityReferences() {

        return entityReferences.values();

    }

}