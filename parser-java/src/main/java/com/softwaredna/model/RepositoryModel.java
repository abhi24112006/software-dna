package com.softwaredna.model;

import com.softwaredna.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.List;

public class RepositoryModel {

    private String repositoryName;

    private List<ParsedFile> files;

    private EntityRegistry entityRegistry;

    private List<Relationship> relationships;

    public RepositoryModel() {

        files = new ArrayList<>();
        entityRegistry = new EntityRegistry();
        relationships = new ArrayList<>();

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

    public List<Relationship> getRelationships() {

        return relationships;

    }

    public void setRelationships(List<Relationship> relationships) {

        this.relationships = relationships;

    }

}