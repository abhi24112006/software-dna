package com.softwaredna.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.softwaredna.ast.ASTGenerator;
import com.softwaredna.builder.ParsedFileBuilder;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.reader.JavaFileReader;

public class ParserApplication {

    public static void main(String[] args) {

        try {

            JavaFileReader reader = new JavaFileReader();

            String sourceCode =
                    reader.readFile("../sample_projects/demo/Student.java");

            ASTGenerator generator = new ASTGenerator();

            CompilationUnit cu =
                    generator.generateAST(sourceCode);

            ParsedFileBuilder builder =
                    new ParsedFileBuilder();

            ParsedFile parsedFile =
                    builder.build(cu);

            System.out.println("=================================");
    System.out.println(" Software DNA Parser Engine");
    System.out.println("=================================");

    System.out.println();

    System.out.println("Package");
    System.out.println(parsedFile.getPackageName());

    System.out.println();

    System.out.println("Imports");

    parsedFile.getImports()
        .forEach(System.out::println);

    System.out.println();

    System.out.println("Classes");

    parsedFile.getClasses()
        .forEach(c -> System.out.println(c.getName()));
        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

}