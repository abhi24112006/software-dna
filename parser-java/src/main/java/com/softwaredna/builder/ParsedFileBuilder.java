package com.softwaredna.builder;

import com.github.javaparser.ast.CompilationUnit;
import com.softwaredna.extractor.ClassExtractor;
import com.softwaredna.extractor.FieldExtractor;
import com.softwaredna.extractor.ImportExtractor;
import com.softwaredna.extractor.MethodExtractor;
import com.softwaredna.extractor.PackageExtractor;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedFile;

import java.util.List;

public class ParsedFileBuilder {

    private final PackageExtractor packageExtractor;
    private final ImportExtractor importExtractor;
    private final ClassExtractor classExtractor;
    private final MethodExtractor methodExtractor;
    private final FieldExtractor fieldExtractor;

    public ParsedFileBuilder() {

        packageExtractor = new PackageExtractor();
        importExtractor = new ImportExtractor();
        classExtractor = new ClassExtractor();
        methodExtractor = new MethodExtractor();
        fieldExtractor = new FieldExtractor();

    }

    public ParsedFile build(CompilationUnit cu) {

        ParsedFile parsedFile = new ParsedFile();

        parsedFile.setPackageName(
                packageExtractor.extractPackageName(cu)
        );

        parsedFile.setImports(
                importExtractor.extractImports(cu)
        );

        List<ParsedClass> classes =
                classExtractor.extractClasses(cu);

        // Temporary implementation:
        // Works correctly because our demo project has one class per file.
        if (!classes.isEmpty()) {

            classes.get(0).setFields(
                    fieldExtractor.extractFields(cu)
            );

            classes.get(0).setMethods(
                    methodExtractor.extractMethods(cu)
            );

        }

        parsedFile.setClasses(classes);

        return parsedFile;

    }

}