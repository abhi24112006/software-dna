package com.softwaredna.extractor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.softwaredna.model.ParsedInterface;

import java.util.ArrayList;
import java.util.List;

public class InterfaceExtractor {

    public List<ParsedInterface> extractInterfaces(CompilationUnit cu) {

        List<ParsedInterface> interfaces = new ArrayList<>();

        for (ClassOrInterfaceDeclaration declaration :
                cu.findAll(ClassOrInterfaceDeclaration.class)) {

            if (declaration.isInterface()) {

                ParsedInterface parsedInterface =
                        new ParsedInterface(
                                declaration.getNameAsString()
                        );

                interfaces.add(parsedInterface);

            }

        }

        return interfaces;

    }

}