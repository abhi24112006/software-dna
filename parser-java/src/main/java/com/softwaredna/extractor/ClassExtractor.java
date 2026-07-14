package com.softwaredna.extractor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.softwaredna.model.ParsedClass;

import java.util.List;

public class ClassExtractor {

    public List<ParsedClass> extractClasses(CompilationUnit cu) {

        return cu.findAll(ClassOrInterfaceDeclaration.class)
                .stream()
                .map(clazz -> new ParsedClass(clazz.getNameAsString()))
                .toList();

    }

}