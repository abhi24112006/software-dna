package com.softwaredna.extractor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.softwaredna.model.ParsedClass;

import java.util.ArrayList;
import java.util.List;

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

        if (!declaration.getExtendedTypes().isEmpty()) {

        parsedClass.setSuperClass(
            declaration.getExtendedTypes()
                    .get(0)
                    .getNameAsString());

        }

            parsedClass.setAnnotations(
                    annotationExtractor.extractAnnotations(
                            declaration));

            classes.add(parsedClass);

        }

        return classes;

    }

}