package com.softwaredna.extractor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.softwaredna.model.ParsedMethod;

import java.util.ArrayList;
import java.util.List;

public class MethodExtractor {

    public List<ParsedMethod> extractMethods(CompilationUnit cu) {

        List<ParsedMethod> methods = new ArrayList<>();

        for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {

            ParsedMethod parsedMethod = new ParsedMethod(
                    method.getNameAsString(),
                    method.getType().asString()
            );

            methods.add(parsedMethod);
        }

        return methods;
    }

}