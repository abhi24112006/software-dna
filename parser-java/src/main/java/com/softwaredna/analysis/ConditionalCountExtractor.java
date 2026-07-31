package com.softwaredna.analysis;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;

public class ConditionalCountExtractor {

    public int extract(MethodDeclaration method) {

        return method.findAll(IfStmt.class).size()
                + method.findAll(SwitchStmt.class).size()
                + method.findAll(ConditionalExpr.class).size();
    }

}