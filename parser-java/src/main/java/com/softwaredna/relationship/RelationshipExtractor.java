package com.softwaredna.relationship;

import com.softwaredna.mapper.EntityReferenceMapper;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.Relationship;
import com.softwaredna.model.RelationshipType;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.resolver.EntityResolver;

public class RelationshipExtractor {

    private final EntityResolver resolver =
            new EntityResolver();

    public void extractRelationships(
            RepositoryModel repository) {

        extractExtends(repository);
        extractImplements(repository);
        extractFieldDependencies(repository);
        extractParameterDependencies(repository);
        extractReturnDependencies(repository);

    }

    /*
     * ------------------------------------------
     * EXTENDS
     * ------------------------------------------
     */

    private void extractExtends(
            RepositoryModel repository) {

        for (ParsedFile file : repository.getFiles()) {

            for (ParsedClass child : file.getClasses()) {

                if (child.getSuperClass() == null
                        || child.getSuperClass().isBlank()) {
                    continue;
                }

                ParsedClass parent =
                        resolver.resolveClass(
                                child.getSuperClass(),
                                repository.getEntityRegistry());

                if (parent == null) {
                    continue;
                }

                Relationship relationship =
                        new Relationship(
                                EntityReferenceMapper.fromClass(child),
                                EntityReferenceMapper.fromClass(parent),
                                RelationshipType.EXTENDS
                        );

                repository.getRelationships()
                        .add(relationship);

            }

        }

    }

    /*
     * ------------------------------------------
     * IMPLEMENTS
     * ------------------------------------------
     */

    private void extractImplements(
            RepositoryModel repository) {

    }

    /*
     * ------------------------------------------
     * FIELD DEPENDENCIES
     * ------------------------------------------
     */

    private void extractFieldDependencies(
            RepositoryModel repository) {

    }

    /*
     * ------------------------------------------
     * PARAMETER DEPENDENCIES
     * ------------------------------------------
     */

    private void extractParameterDependencies(
            RepositoryModel repository) {

    }

    /*
     * ------------------------------------------
     * RETURN DEPENDENCIES
     * ------------------------------------------
     */

    private void extractReturnDependencies(
            RepositoryModel repository) {

    }

}