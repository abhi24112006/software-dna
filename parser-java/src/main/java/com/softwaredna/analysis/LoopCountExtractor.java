package com.softwaredna.analysis;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.WhileStmt;

public class LoopCountExtractor {

    public int extract(MethodDeclaration method) {

        return method.findAll(ForStmt.class).size()
                + method.findAll(ForEachStmt.class).size()
                + method.findAll(WhileStmt.class).size()
                + method.findAll(DoStmt.class).size();
    }

}