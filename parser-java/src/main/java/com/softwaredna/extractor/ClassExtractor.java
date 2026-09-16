package com.softwaredna.extractor;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.softwaredna.model.ParsedClass;

public class ClassExtractor {

    private final AnnotationExtractor annotationExtractor =
            new AnnotationExtractor();


    public List<ParsedClass> extractClasses(
            CompilationUnit cu) {

        List<ParsedClass> classes =
                new ArrayList<>();


        for (ClassOrInterfaceDeclaration declaration :
                cu.findAll(ClassOrInterfaceDeclaration.class)) {

            if (declaration.isInterface()) {
                continue;
            }


            ParsedClass parsedClass =
                    new ParsedClass();


            parsedClass.setName(
                    declaration.getNameAsString());


            /*
             * Capture the source line where the class
             * declaration begins.
             *
             * This information is kept in the
             * language-independent ParsedClass model
             * and can later be used as source evidence
             * for relationships such as EXTENDS.
             */
            if (declaration.getBegin().isPresent()) {

                parsedClass.setSourceLineNumber(
                        declaration.getBegin()
                                .get()
                                .line
                );
            }


            if (!declaration.getExtendedTypes().isEmpty()) {

                parsedClass.setSuperClass(
                        declaration.getExtendedTypes()
                                .get(0)
                                .getNameAsString());

            }


            List<String> interfaces =
                    new ArrayList<>();


            declaration.getImplementedTypes()
                    .forEach(type ->
                            interfaces.add(
                                    type.getNameAsString()));


            parsedClass.setImplementedInterfaces(
                    interfaces);


            parsedClass.setAnnotations(
                    annotationExtractor.extractAnnotations(
                            declaration));


            classes.add(parsedClass);

        }


        return classes;

    }

}