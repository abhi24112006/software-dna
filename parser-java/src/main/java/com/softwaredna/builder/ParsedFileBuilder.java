package com.softwaredna.builder;

import com.github.javaparser.ast.CompilationUnit;
import com.softwaredna.extractor.ClassExtractor;
import com.softwaredna.extractor.ImportExtractor;
import com.softwaredna.extractor.PackageExtractor;
import com.softwaredna.model.ParsedFile;

public class ParsedFileBuilder {

    private final PackageExtractor packageExtractor;
    private final ImportExtractor importExtractor;
    private final ClassExtractor classExtractor;

    public ParsedFileBuilder() {

        packageExtractor = new PackageExtractor();
        importExtractor = new ImportExtractor();
        classExtractor = new ClassExtractor();

    }

    public ParsedFile build(CompilationUnit cu) {

        ParsedFile parsedFile = new ParsedFile();

        parsedFile.setPackageName(
                packageExtractor.extractPackageName(cu)
        );

        parsedFile.setImports(
                importExtractor.extractImports(cu)
        );

        parsedFile.setClasses(
                classExtractor.extractClasses(cu)
        );

        return parsedFile;

    }

}