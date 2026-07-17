package com.softwaredna.extractor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.softwaredna.model.ParsedMethod;

import java.util.ArrayList;
import java.util.List;

public class MethodExtractor {

    private final ParameterExtractor parameterExtractor =
            new ParameterExtractor();

    public List<ParsedMethod> extractMethods(
            ClassOrInterfaceDeclaration clazz) {

        List<ParsedMethod> methods =
                new ArrayList<>();

        for (MethodDeclaration method : clazz.getMethods()) {

            ParsedMethod parsedMethod =
                    new ParsedMethod();

            parsedMethod.setName(
                    method.getNameAsString());

            parsedMethod.setReturnType(
                    method.getType().asString());

            parsedMethod.setParameters(
                    parameterExtractor.extractParameters(method));

            methods.add(parsedMethod);

        }

        return methods;

    }

}