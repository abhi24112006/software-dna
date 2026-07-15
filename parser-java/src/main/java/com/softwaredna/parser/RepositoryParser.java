package com.softwaredna.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.softwaredna.ast.ASTGenerator;
import com.softwaredna.builder.ParsedFileBuilder;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.reader.JavaFileReader;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class RepositoryParser {

    private final JavaFileReader reader;
    private final ASTGenerator generator;
    private final ParsedFileBuilder builder;

    public RepositoryParser() {

        reader = new JavaFileReader();
        generator = new ASTGenerator();
        builder = new ParsedFileBuilder();

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

                            System.out.println("================================");
                            System.out.println(path);
                            System.out.println(source);

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

        return repository;

    }

}