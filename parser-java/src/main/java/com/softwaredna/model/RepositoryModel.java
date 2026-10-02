package com.softwaredna.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.softwaredna.registry.EntityRegistry;

public class RepositoryModel {

    private String repositoryName;

    private List<ParsedFile> files;

    private EntityRegistry entityRegistry;

    private Set<Relationship> relationships;

    private RepositoryMetrics metrics;

    public RepositoryModel() {

        files = new ArrayList<>();

        entityRegistry = new EntityRegistry();

        relationships = new LinkedHashSet<>();

        metrics = null;
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

    public RepositoryMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(RepositoryMetrics metrics) {
        this.metrics = metrics;
    }
}