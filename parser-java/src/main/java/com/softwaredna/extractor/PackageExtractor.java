package com.softwaredna.extractor;

import com.github.javaparser.ast.CompilationUnit;

public class PackageExtractor {

    public String extractPackageName(CompilationUnit cu) {

        return cu.getPackageDeclaration()
                .map(pkg -> pkg.getNameAsString())
                .orElse("Default Package");

    }

}