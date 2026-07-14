package com.softwaredna.extractor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;

import java.util.List;

public class ImportExtractor {

    public List<String> extractImports(CompilationUnit cu) {

        return cu.getImports()
                .stream()
                .map(ImportDeclaration::getNameAsString)
                .toList();

    }

}