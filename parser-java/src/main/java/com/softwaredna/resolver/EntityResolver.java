package com.softwaredna.resolver;

import com.softwaredna.model.ParsedClass;
import com.softwaredna.registry.EntityRegistry;

public class EntityResolver {

    public ParsedClass resolveClass(
            String className,
            EntityRegistry registry) {

        if (className == null || className.isBlank()) {
            return null;
        }

        return registry.findClassByName(className);

    }

}