package com.softwaredna.resolver;

import com.softwaredna.mapper.EntityReferenceMapper;
import com.softwaredna.model.EntityReference;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedInterface;
import com.softwaredna.registry.EntityRegistry;

import java.util.List;

public class EntityResolver {

    /*
     * -------------------------------------------------------
     * Class Resolution
     * -------------------------------------------------------
     */

    public ParsedClass resolveClass(
            String className,
            EntityRegistry registry) {

        return resolveClass(
                className,
                null,
                null,
                registry);

    }


    public ParsedClass resolveClass(
            String className,
            String packageName,
            EntityRegistry registry) {

        return resolveClass(
                className,
                packageName,
                null,
                registry);

    }


    public ParsedClass resolveClass(
            String className,
            String packageName,
            List<String> imports,
            EntityRegistry registry) {

        if (className == null
                || className.isBlank()) {

            return null;

        }

        className = cleanTypeName(className);


        /*
         * 1. Fully qualified name.
         */

        if (className.contains(".")) {

            ParsedClass parsedClass =
                    registry.findClassByQualifiedName(
                            className);

            if (parsedClass != null) {
                return parsedClass;
            }

        }


        /*
         * 2. Explicit import.
         */

        String importedName =
                findExplicitImport(
                        className,
                        imports);

        if (importedName != null) {

            ParsedClass parsedClass =
                    registry.findClassByQualifiedName(
                            importedName);

            if (parsedClass != null) {
                return parsedClass;
            }

        }


        /*
         * 3. Same package.
         */

        ParsedClass parsedClass =
                registry.findClassByName(
                        className,
                        packageName);

        if (parsedClass != null) {
            return parsedClass;
        }


        /*
         * 4. Wildcard imports.
         */

        String wildcardPackage =
                findWildcardImportPackage(
                        imports);

        if (wildcardPackage != null) {

            parsedClass =
                    registry.findClassByName(
                            className,
                            wildcardPackage);

            if (parsedClass != null) {
                return parsedClass;
            }

        }


        /*
         * 5. Unambiguous global lookup.
         */

        return registry.findClassByName(
                className);

    }


    /*
     * -------------------------------------------------------
     * Interface Resolution
     * -------------------------------------------------------
     */

    public ParsedInterface resolveInterface(
            String interfaceName,
            EntityRegistry registry) {

        return resolveInterface(
                interfaceName,
                null,
                null,
                registry);

    }


    public ParsedInterface resolveInterface(
            String interfaceName,
            String packageName,
            EntityRegistry registry) {

        return resolveInterface(
                interfaceName,
                packageName,
                null,
                registry);

    }


    public ParsedInterface resolveInterface(
            String interfaceName,
            String packageName,
            List<String> imports,
            EntityRegistry registry) {

        if (interfaceName == null
                || interfaceName.isBlank()) {

            return null;

        }

        interfaceName =
                cleanTypeName(interfaceName);


        /*
         * 1. Fully qualified name.
         */

        if (interfaceName.contains(".")) {

            ParsedInterface parsedInterface =
                    registry.findInterfaceByQualifiedName(
                            interfaceName);

            if (parsedInterface != null) {
                return parsedInterface;
            }

        }


        /*
         * 2. Explicit import.
         */

        String importedName =
                findExplicitImport(
                        interfaceName,
                        imports);

        if (importedName != null) {

            ParsedInterface parsedInterface =
                    registry.findInterfaceByQualifiedName(
                            importedName);

            if (parsedInterface != null) {
                return parsedInterface;
            }

        }


        /*
         * 3. Same package.
         */

        ParsedInterface parsedInterface =
                registry.findInterfaceByName(
                        interfaceName,
                        packageName);

        if (parsedInterface != null) {
            return parsedInterface;
        }


        /*
         * 4. Wildcard imports.
         */

        String wildcardPackage =
                findWildcardImportPackage(
                        imports);

        if (wildcardPackage != null) {

            parsedInterface =
                    registry.findInterfaceByName(
                            interfaceName,
                            wildcardPackage);

            if (parsedInterface != null) {
                return parsedInterface;
            }

        }


        /*
         * 5. Unambiguous global lookup.
         */

        return registry.findInterfaceByName(
                interfaceName);

    }


    /*
     * -------------------------------------------------------
     * Generic Type Resolution
     * -------------------------------------------------------
     */

    public EntityReference resolveType(
            String typeName,
            EntityRegistry registry) {

        return resolveType(
                typeName,
                null,
                null,
                registry);

    }


    public EntityReference resolveType(
            String typeName,
            String packageName,
            EntityRegistry registry) {

        return resolveType(
                typeName,
                packageName,
                null,
                registry);

    }


    public EntityReference resolveType(
            String typeName,
            String packageName,
            List<String> imports,
            EntityRegistry registry) {

        if (typeName == null
                || typeName.isBlank()) {

            return null;

        }


        /*
         * Try class.
         */

        ParsedClass parsedClass =
                resolveClass(
                        typeName,
                        packageName,
                        imports,
                        registry);

        if (parsedClass != null) {

            return EntityReferenceMapper.fromClass(
                    parsedClass);

        }


        /*
         * Try interface.
         */

        ParsedInterface parsedInterface =
                resolveInterface(
                        typeName,
                        packageName,
                        imports,
                        registry);

        if (parsedInterface != null) {

            return EntityReferenceMapper.fromInterface(
                    parsedInterface);

        }


        return null;

    }


    /*
     * -------------------------------------------------------
     * Import Helpers
     * -------------------------------------------------------
     */

    private String findExplicitImport(
            String typeName,
            List<String> imports) {

        if (imports == null) {
            return null;
        }

        for (String imported :
                imports) {

            if (imported == null
                    || imported.isBlank()) {
                continue;
            }

            imported =
                    imported.trim();

            /*
             * Ignore wildcard imports here.
             */

            if (imported.endsWith(".*")) {
                continue;
            }

            int index =
                    imported.lastIndexOf('.');

            if (index < 0) {
                continue;
            }

            String importedSimpleName =
                    imported.substring(
                            index + 1);

            if (typeName.equals(
                    importedSimpleName)) {

                return imported;

            }

        }

        return null;

    }


    private String findWildcardImportPackage(
            List<String> imports) {

        if (imports == null) {
            return null;
        }

        for (String imported :
                imports) {

            if (imported == null
                    || imported.isBlank()) {
                continue;
            }

            imported =
                    imported.trim();

            if (imported.endsWith(".*")) {

                return imported.substring(
                        0,
                        imported.length() - 2);

            }

        }

        return null;

    }


    /*
     * -------------------------------------------------------
     * Type Cleaning
     * -------------------------------------------------------
     */

    private String cleanTypeName(
            String typeName) {

        String cleaned =
                typeName.trim();

        /*
         * Remove generic information.
         *
         * Example:
         *
         * List<Student>
         *       ↓
         * List
         */

        int genericIndex =
                cleaned.indexOf('<');

        if (genericIndex >= 0) {

            cleaned =
                    cleaned.substring(
                            0,
                            genericIndex);

        }

        /*
         * Remove array suffix.
         */

        while (cleaned.endsWith("[]")) {

            cleaned =
                    cleaned.substring(
                            0,
                            cleaned.length() - 2);

        }

        /*
         * Remove varargs.
         */

        if (cleaned.endsWith("...")) {

            cleaned =
                    cleaned.substring(
                            0,
                            cleaned.length() - 3);

        }

        return cleaned.trim();

    }

}