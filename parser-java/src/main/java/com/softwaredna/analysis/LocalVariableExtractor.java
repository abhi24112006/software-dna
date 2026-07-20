package com.softwaredna.analysis;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.softwaredna.scope.Scope;

public class LocalVariableExtractor {

    public void extract(
            MethodDeclaration method,
            Scope scope) {

        method.findAll(VariableDeclarationExpr.class)
                .forEach(variableDeclaration -> {

                    for (VariableDeclarator variable :
                            variableDeclaration.getVariables()) {

                        scope.declareVariable(
                                variable.getNameAsString(),
                                variable.getTypeAsString());

                    }

                });

    }

}