package com.softwaredna.registry;

import com.softwaredna.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
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

    private final Map<String, List<ParsedClass>> classesByName =
            new LinkedHashMap<>();

    private final Map<String, List<ParsedInterface>> interfacesByName =
            new LinkedHashMap<>();


    /*
     * -------------------------------------------------------
     * Registration Methods
     * -------------------------------------------------------
     */

    public void registerClass(
            ParsedClass parsedClass) {

        parsedEntitiesById.put(
                parsedClass.getId(),
                parsedClass
        );

        classesByName.computeIfAbsent(
                parsedClass.getName(),
                ignored -> new ArrayList<>()
        ).add(parsedClass);

    }


    public void registerInterface(
            ParsedInterface parsedInterface) {

        parsedEntitiesById.put(
                parsedInterface.getId(),
                parsedInterface
        );

        interfacesByName.computeIfAbsent(
                parsedInterface.getName(),
                ignored -> new ArrayList<>()
        ).add(parsedInterface);

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
     * Class Lookup
     * -------------------------------------------------------
     */

    public List<ParsedClass> findClassesByName(
            String name) {

        List<ParsedClass> candidates =
                classesByName.get(name);

        if (candidates == null
                || candidates.isEmpty()) {

            return Collections.emptyList();

        }

        return candidates;

    }


    /*
     * Simple lookup.
     *
     * Only succeeds when unambiguous.
     */

    public ParsedClass findClassByName(
            String name) {

        List<ParsedClass> candidates =
                findClassesByName(name);

        if (candidates.size() == 1) {

            return candidates.get(0);

        }

        return null;

    }


    /*
     * Package-aware lookup.
     *
     * Resolution:
     *
     * 1. Same package
     * 2. Exactly one candidate
     * 3. Default package
     * 4. Ambiguous -> null
     */

    public ParsedClass findClassByName(
            String name,
            String packageName) {

        List<ParsedClass> candidates =
                classesByName.get(name);

        if (candidates == null
                || candidates.isEmpty()) {

            return null;

        }


        /*
         * 1. Same package.
         */

        if (packageName != null
                && !packageName.isBlank()) {

            for (ParsedClass candidate :
                    candidates) {

                if (packageName.equals(
                        candidate.getPackageName())) {

                    return candidate;

                }

            }

        }


        /*
         * 2. Exactly one candidate.
         */

        if (candidates.size() == 1) {

            return candidates.get(0);

        }


        /*
         * 3. Default package candidate.
         */

        ParsedClass defaultPackageCandidate = null;

        for (ParsedClass candidate :
                candidates) {

            String candidatePackage =
                    candidate.getPackageName();

            if (candidatePackage == null
                    || candidatePackage.isBlank()) {

                if (defaultPackageCandidate != null) {

                    return null;

                }

                defaultPackageCandidate = candidate;

            }

        }

        if (defaultPackageCandidate != null) {

            return defaultPackageCandidate;

        }


        /*
         * 4. Ambiguous.
         */

        return null;

    }


    /*
     * -------------------------------------------------------
     * Fully Qualified Class Lookup
     * -------------------------------------------------------
     */

    public ParsedClass findClassByQualifiedName(
            String qualifiedName) {

        if (qualifiedName == null
                || qualifiedName.isBlank()) {

            return null;

        }

        List<ParsedClass> candidates =
                findClassesByName(
                        simpleName(qualifiedName));

        for (ParsedClass candidate :
                candidates) {

            if (qualifiedName.equals(
                    candidate.getId())) {

                return candidate;

            }

        }

        return null;

    }


    /*
     * -------------------------------------------------------
     * Interface Lookup
     * -------------------------------------------------------
     */

    public List<ParsedInterface> findInterfacesByName(
            String name) {

        List<ParsedInterface> candidates =
                interfacesByName.get(name);

        if (candidates == null
                || candidates.isEmpty()) {

            return Collections.emptyList();

        }

        return candidates;

    }


    /*
     * Simple interface lookup.
     *
     * Only succeeds when unambiguous.
     */

    public ParsedInterface findInterfaceByName(
            String name) {

        List<ParsedInterface> candidates =
                findInterfacesByName(name);

        if (candidates.size() == 1) {

            return candidates.get(0);

        }

        return null;

    }


    /*
     * Package-aware interface lookup.
     */

    public ParsedInterface findInterfaceByName(
            String name,
            String packageName) {

        List<ParsedInterface> candidates =
                interfacesByName.get(name);

        if (candidates == null
                || candidates.isEmpty()) {

            return null;

        }


        /*
         * 1. Same package.
         */

        if (packageName != null
                && !packageName.isBlank()) {

            for (ParsedInterface candidate :
                    candidates) {

                String candidateId =
                        candidate.getId();

                if (candidateId == null) {
                    continue;
                }

                String expectedPrefix =
                        packageName + ".";

                if (candidateId.startsWith(
                        expectedPrefix)) {

                    return candidate;

                }

            }

        }


        /*
         * 2. Exactly one candidate.
         */

        if (candidates.size() == 1) {

            return candidates.get(0);

        }


        /*
         * 3. Ambiguous.
         */

        return null;

    }


    /*
     * -------------------------------------------------------
     * Fully Qualified Interface Lookup
     * -------------------------------------------------------
     */

    public ParsedInterface findInterfaceByQualifiedName(
            String qualifiedName) {

        if (qualifiedName == null
                || qualifiedName.isBlank()) {

            return null;

        }

        List<ParsedInterface> candidates =
                findInterfacesByName(
                        simpleName(qualifiedName));

        for (ParsedInterface candidate :
                candidates) {

            if (qualifiedName.equals(
                    candidate.getId())) {

                return candidate;

            }

        }

        return null;

    }


    /*
     * -------------------------------------------------------
     * Entity Reference Lookup
     * -------------------------------------------------------
     */

    public EntityReference findEntityReferenceById(
            String id) {

        return entityReferences.get(id);

    }


    public EntityReference findClassEntityByName(
            String className) {

        ParsedClass parsedClass =
                findClassByName(className);

        if (parsedClass == null) {
            return null;
        }

        return findEntityReferenceById(
                parsedClass.getId());

    }


    /*
     * -------------------------------------------------------
     * Collection Access
     * -------------------------------------------------------
     */

    public Collection<Object> getAllParsedEntities() {

        return parsedEntitiesById.values();

    }


    public Collection<EntityReference> getAllEntityReferences() {

        return entityReferences.values();

    }


    /*
     * -------------------------------------------------------
     * Utility
     * -------------------------------------------------------
     */

    private String simpleName(
            String qualifiedName) {

        int index =
                qualifiedName.lastIndexOf('.');

        if (index < 0) {
            return qualifiedName;
        }

        return qualifiedName.substring(
                index + 1);

    }

}