package com.softwaredna.builder;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.softwaredna.extractor.*;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedFile;

import java.util.List;

public class ParsedFileBuilder {

    private final PackageExtractor packageExtractor;
    private final ImportExtractor importExtractor;
    private final ClassExtractor classExtractor;
    private final MethodExtractor methodExtractor;
    private final FieldExtractor fieldExtractor;
    private final ConstructorExtractor constructorExtractor;
    private final InterfaceExtractor interfaceExtractor;
    private final EnumExtractor enumExtractor;

    public ParsedFileBuilder() {

        packageExtractor = new PackageExtractor();
        importExtractor = new ImportExtractor();
        classExtractor = new ClassExtractor();
        methodExtractor = new MethodExtractor();
        fieldExtractor = new FieldExtractor();
        constructorExtractor = new ConstructorExtractor();
        interfaceExtractor = new InterfaceExtractor();
        enumExtractor = new EnumExtractor();

    }

    public ParsedFile build(CompilationUnit cu) {

        ParsedFile parsedFile = new ParsedFile();

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

        List<ParsedClass> classes =
                classExtractor.extractClasses(cu);

        List<ClassOrInterfaceDeclaration> declarations =
                cu.findAll(ClassOrInterfaceDeclaration.class);

        int classIndex = 0;

        for (ClassOrInterfaceDeclaration declaration : declarations) {

            if (!declaration.isInterface()) {

                ParsedClass parsedClass = classes.get(classIndex);

                parsedClass.setFields(
                        fieldExtractor.extractFields(declaration)
                );

                parsedClass.setConstructors(
                        constructorExtractor.extractConstructors(declaration)
                );

                parsedClass.setMethods(
                        methodExtractor.extractMethods(declaration)
                );

                classIndex++;

            }

        }

        parsedFile.setClasses(classes);

        return parsedFile;

    }

}