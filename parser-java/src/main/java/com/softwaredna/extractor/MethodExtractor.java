package com.softwaredna.extractor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.softwaredna.model.ParsedMethod;

import java.util.ArrayList;
import java.util.List;

public class MethodExtractor {

    public List<ParsedMethod> extractMethods(
            ClassOrInterfaceDeclaration classDeclaration) {

        List<ParsedMethod> methods = new ArrayList<>();

        for (MethodDeclaration method :
                classDeclaration.getMethods()) {

            ParsedMethod parsedMethod =
                    new ParsedMethod(

                            method.getNameAsString(),

                            method.getType().asString()

                    );

            methods.add(parsedMethod);

        }

        return methods;

    }

}