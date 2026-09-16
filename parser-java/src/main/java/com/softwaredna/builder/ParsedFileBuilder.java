package com.softwaredna.builder;

import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.softwaredna.analysis.metrics.ClassMetricAggregator;
import com.softwaredna.extractor.AnnotationExtractor;
import com.softwaredna.extractor.ClassExtractor;
import com.softwaredna.extractor.ConstructorExtractor;
import com.softwaredna.extractor.EnumExtractor;
import com.softwaredna.extractor.FieldExtractor;
import com.softwaredna.extractor.ImportExtractor;
import com.softwaredna.extractor.InterfaceExtractor;
import com.softwaredna.extractor.MethodExtractor;
import com.softwaredna.extractor.PackageExtractor;
import com.softwaredna.extractor.RecordExtractor;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedEnum;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.ParsedInterface;
import com.softwaredna.model.ParsedRecord;

public class ParsedFileBuilder {

    private final PackageExtractor packageExtractor;
    private final ImportExtractor importExtractor;
    private final ClassExtractor classExtractor;
    private final MethodExtractor methodExtractor;
    private final FieldExtractor fieldExtractor;
    private final ConstructorExtractor constructorExtractor;
    private final InterfaceExtractor interfaceExtractor;
    private final EnumExtractor enumExtractor;
    private final RecordExtractor recordExtractor;
    private final AnnotationExtractor annotationExtractor;
    private final ClassMetricAggregator classMetricAggregator;

    public ParsedFileBuilder() {

        packageExtractor = new PackageExtractor();
        importExtractor = new ImportExtractor();
        classExtractor = new ClassExtractor();
        methodExtractor = new MethodExtractor();
        fieldExtractor = new FieldExtractor();
        constructorExtractor = new ConstructorExtractor();
        interfaceExtractor = new InterfaceExtractor();
        enumExtractor = new EnumExtractor();
        recordExtractor = new RecordExtractor();
        annotationExtractor = new AnnotationExtractor();
        classMetricAggregator = new ClassMetricAggregator();
    }

    public ParsedFile build(
            CompilationUnit cu,
            String sourcePath) {

        ParsedFile parsedFile = new ParsedFile();

        parsedFile.setSourcePath(sourcePath);

        parsedFile.setPackageName(
                packageExtractor.extractPackageName(cu)
        );

        parsedFile.setImports(
                importExtractor.extractImports(cu)
        );

        parsedFile.setInterfaces(
                interfaceExtractor.extractInterfaces(cu)
        );

        parsedFile.setEnums(
                enumExtractor.extractEnums(cu)
        );

        parsedFile.setRecords(
                recordExtractor.extractRecords(cu)
        );

        // ---------------- CLASSES ----------------

        List<ParsedClass> classes =
                classExtractor.extractClasses(cu);

        List<ClassOrInterfaceDeclaration> declarations =
                cu.findAll(ClassOrInterfaceDeclaration.class);

        int classIndex = 0;

        for (ClassOrInterfaceDeclaration declaration :
                declarations) {

            if (!declaration.isInterface()) {

                ParsedClass parsedClass =
                        classes.get(classIndex);

                parsedClass.setPackageName(
                        parsedFile.getPackageName()
                );

                parsedClass.setFields(
                        fieldExtractor.extractFields(
                                declaration
                        )
                );

                parsedClass.setConstructors(
                        constructorExtractor.extractConstructors(
                                declaration
                        )
                );

                parsedClass.setMethods(
                        methodExtractor.extractMethods(
                                declaration
                        )
                );

                parsedClass.setMetrics(
                        classMetricAggregator.aggregate(
                                parsedClass
                        )
                );

                classIndex++;
            }
        }

        parsedFile.setClasses(classes);

        // ---------------- INTERFACES ----------------

        List<ClassOrInterfaceDeclaration> interfaceDeclarations =
                declarations.stream()
                        .filter(ClassOrInterfaceDeclaration::isInterface)
                        .toList();

        List<ParsedInterface> interfaces =
                parsedFile.getInterfaces();

        for (int i = 0;
             i < interfaceDeclarations.size();
             i++) {

            interfaces.get(i).setAnnotations(
                    annotationExtractor.extractAnnotations(
                            interfaceDeclarations.get(i)
                    )
            );
        }

        // ---------------- ENUMS ----------------

        List<EnumDeclaration> enumDeclarations =
                cu.findAll(EnumDeclaration.class);

        List<ParsedEnum> enums =
                parsedFile.getEnums();

        for (int i = 0;
             i < enumDeclarations.size();
             i++) {

            enums.get(i).setAnnotations(
                    annotationExtractor.extractAnnotations(
                            enumDeclarations.get(i)
                    )
            );
        }

        // ---------------- RECORDS ----------------

        List<RecordDeclaration> recordDeclarations =
                cu.findAll(RecordDeclaration.class);

        List<ParsedRecord> records =
                parsedFile.getRecords();

        for (int i = 0;
             i < recordDeclarations.size();
             i++) {

            records.get(i).setAnnotations(
                    annotationExtractor.extractAnnotations(
                            recordDeclarations.get(i)
                    )
            );
        }

        return parsedFile;
    }
}