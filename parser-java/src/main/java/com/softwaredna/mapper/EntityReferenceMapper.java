package com.softwaredna.mapper;

import com.softwaredna.model.EntityReference;
import com.softwaredna.model.EntityType;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedInterface;
import com.softwaredna.model.ParsedMethod;

public class EntityReferenceMapper {

    private EntityReferenceMapper() {

    }

    public static EntityReference fromClass(
            ParsedClass parsedClass) {

        return new EntityReference(
                parsedClass.getId(),
                parsedClass.getName(),
                EntityType.CLASS
        );

    }

    public static EntityReference fromInterface(
            ParsedInterface parsedInterface) {

        return new EntityReference(
                parsedInterface.getId(),
                parsedInterface.getName(),
                EntityType.INTERFACE
        );

    }

    public static EntityReference fromMethod(
            ParsedMethod parsedMethod) {

        return new EntityReference(
                parsedMethod.getId(),
                parsedMethod.getName(),
                EntityType.METHOD
        );

    }

}