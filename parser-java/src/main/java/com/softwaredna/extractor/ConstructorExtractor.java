package com.softwaredna.extractor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.softwaredna.model.ParsedConstructor;

import java.util.ArrayList;
import java.util.List;

public class ConstructorExtractor {

    private final ParameterExtractor parameterExtractor =
            new ParameterExtractor();

    public List<ParsedConstructor> extractConstructors(
            ClassOrInterfaceDeclaration clazz) {

        List<ParsedConstructor> constructors =
                new ArrayList<>();

        for (ConstructorDeclaration constructor :
                clazz.getConstructors()) {

            ParsedConstructor parsedConstructor =
                    new ParsedConstructor();

            parsedConstructor.setName(
                    constructor.getNameAsString());

            parsedConstructor.setParameters(
                    parameterExtractor.extractParameters(constructor));

            constructors.add(parsedConstructor);

        }

        return constructors;

    }

}