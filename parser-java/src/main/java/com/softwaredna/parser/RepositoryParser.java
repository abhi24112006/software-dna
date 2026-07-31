package com.softwaredna.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.softwaredna.ast.ASTGenerator;
import com.softwaredna.builder.ParsedFileBuilder;
import com.softwaredna.identifier.IdentifierAssigner;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.reader.JavaFileReader;
import com.softwaredna.registry.EntityRegistrar;
import com.softwaredna.relationship.RelationshipExtractor;
import com.softwaredna.analysis.repository.RepositoryAnalyzer;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class RepositoryParser {

    private final JavaFileReader reader;
    private final ASTGenerator generator;
    private final ParsedFileBuilder builder;

    private final IdentifierAssigner identifierAssigner;
    private final EntityRegistrar registrar;
    private final RelationshipExtractor relationshipExtractor;
    private final RepositoryAnalyzer repositoryAnalyzer;

    public RepositoryParser() {

        reader = new JavaFileReader();
        generator = new ASTGenerator();
        builder = new ParsedFileBuilder();

        identifierAssigner = new IdentifierAssigner();
        registrar = new EntityRegistrar();
        relationshipExtractor = new RelationshipExtractor();
        repositoryAnalyzer = new RepositoryAnalyzer();

    }

    public RepositoryModel parseRepository(String repositoryPath)
            throws IOException {

        RepositoryModel repository = new RepositoryModel();

        Path repoPath = Paths.get(repositoryPath);

        repository.setRepositoryName(
                repoPath.getFileName().toString()
        );

        try (Stream<Path> paths = Files.walk(repoPath)) {

            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {

                        try {

                            String source =
                                    reader.readFile(path.toString());

                            CompilationUnit cu =
                                    generator.generateAST(source);

                            ParsedFile parsedFile =
                                    builder.build(cu);

                            repository.getFiles().add(parsedFile);

                        }

                        catch (Exception e) {

                            System.out.println(
                                    "Failed to parse: "
                                            + path
                            );

                            e.printStackTrace();

                        }

                    });

        }

        /*
         * Phase 1
         * Assign IDs
         */
        identifierAssigner.assignIds(repository);

        /*
         * Phase 2
         * Register entities
         */
        registrar.registerEntities(repository);

        /*
         * Phase 3
         * Extract relationships
         */
        relationshipExtractor.extractRelationships(repository);


        /*
            * Phase 4
            * Analyze repository
        */
        repositoryAnalyzer.analyze(repository);
        return repository;

    }

}