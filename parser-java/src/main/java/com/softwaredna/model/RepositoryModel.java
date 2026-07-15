package com.softwaredna.model;

import java.util.ArrayList;
import java.util.List;

public class RepositoryModel {

    private String repositoryName;

    private List<ParsedFile> files;

    public RepositoryModel() {

        files = new ArrayList<>();

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

}