package com.softwaredna.model;

import com.softwaredna.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RepositoryModel {

    private String repositoryName;

    private List<ParsedFile> files;

    private EntityRegistry entityRegistry;

    private Set<Relationship> relationships;

    public RepositoryModel() {

        files = new ArrayList<>();
        entityRegistry = new EntityRegistry();
        relationships = new LinkedHashSet<>();

    }

    public String getRepositoryName() {

        return repositoryName;

    }

    public void setRepositoryName(String repositoryName) {

        this.repositoryName = repositoryName;

    }

    public List<ParsedFile> getFiles() {

        return files;

    }

    public void setFiles(List<ParsedFile> files) {

        this.files = files;

    }

    public EntityRegistry getEntityRegistry() {

        return entityRegistry;

    }

    public void setEntityRegistry(EntityRegistry entityRegistry) {

        this.entityRegistry = entityRegistry;

    }

    public Set<Relationship> getRelationships() {

        return relationships;

    }

    public void setRelationships(Set<Relationship> relationships) {

        this.relationships = relationships;

    }

}