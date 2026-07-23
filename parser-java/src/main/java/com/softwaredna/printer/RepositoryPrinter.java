package com.softwaredna.printer;

import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.RepositoryModel;

public class RepositoryPrinter {

    private final FilePrinter filePrinter;
    private final RelationshipPrinter relationshipPrinter;

    public RepositoryPrinter() {

        filePrinter = new FilePrinter();
        relationshipPrinter = new RelationshipPrinter();

    }

    public void print(RepositoryModel repository) {

        System.out.println("======================================");
        System.out.println("     Software DNA Repository Engine");
        System.out.println("======================================");
        System.out.println();

        System.out.println("Repository : "
                + repository.getRepositoryName());

        System.out.println("Files Parsed : "
                + repository.getFiles().size());

        System.out.println();

        for (ParsedFile file : repository.getFiles()) {

            filePrinter.print(file);

        }

        relationshipPrinter.print(
                repository.getRelationships()
        );

    }

}