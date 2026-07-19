package com.softwaredna.mapper;

import com.softwaredna.model.EntityReference;
import com.softwaredna.model.EntityType;
import com.softwaredna.model.ParsedClass;

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

}