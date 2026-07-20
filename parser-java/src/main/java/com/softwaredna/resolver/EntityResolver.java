package com.softwaredna.resolver;

import com.softwaredna.mapper.EntityReferenceMapper;
import com.softwaredna.model.EntityReference;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedInterface;
import com.softwaredna.registry.EntityRegistry;

public class EntityResolver {

    public ParsedClass resolveClass(
            String className,
            EntityRegistry registry) {

        return resolveClass(className, null, registry);

    }

    public ParsedClass resolveClass(
            String className,
            String packageName,
            EntityRegistry registry) {

        if (className == null || className.isBlank()) {
            return null;
        }

        return registry.findClassByName(className, packageName);

    }

    public ParsedInterface resolveInterface(
            String interfaceName,
            EntityRegistry registry) {

        if (interfaceName == null || interfaceName.isBlank()) {
            return null;
        }

        return registry.findInterfaceByName(interfaceName);

    }

    public EntityReference resolveType(
            String typeName,
            EntityRegistry registry) {

        if (typeName == null || typeName.isBlank()) {
            return null;
        }

        ParsedClass parsedClass =
                resolveClass(typeName, registry);

        if (parsedClass != null) {

            return EntityReferenceMapper.fromClass(parsedClass);

        }

        ParsedInterface parsedInterface =
                resolveInterface(typeName, registry);

        if (parsedInterface != null) {

            return EntityReferenceMapper.fromInterface(parsedInterface);

        }

        return null;

    }

}