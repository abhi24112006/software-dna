package com.softwaredna.extractor;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.softwaredna.scope.Scope;

public class LocalVariableExtractor {

    public void extract(
            MethodDeclaration method,
            Scope scope) {

        if (method == null || scope == null) {
            return;
        }

        method.findAll(VariableDeclarator.class)
                .forEach(variable -> {

                    scope.declareVariable(
                            variable.getNameAsString(),
                            variable.getType().asString()
                    );

                });

    }

}