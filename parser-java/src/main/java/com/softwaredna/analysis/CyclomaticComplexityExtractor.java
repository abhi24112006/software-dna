package com.softwaredna.analysis;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.stmt.DoStmt;

public class CyclomaticComplexityExtractor {

    public int extract(MethodDeclaration method) {

        int complexity = 1;

        complexity += method.findAll(IfStmt.class).size();

        complexity += method.findAll(ForStmt.class).size();

        complexity += method.findAll(ForEachStmt.class).size();

        complexity += method.findAll(WhileStmt.class).size();

        complexity += method.findAll(DoStmt.class).size();

        complexity += method.findAll(CatchClause.class).size();

        complexity += method.findAll(ConditionalExpr.class).size();

        // Count switch cases (ignore default)
        for (SwitchEntry entry : method.findAll(SwitchEntry.class)) {

            if (!entry.getLabels().isEmpty()) {

                complexity++;

            }

        }

        // Count && and ||
        for (BinaryExpr binaryExpr : method.findAll(BinaryExpr.class)) {

            if (binaryExpr.getOperator() == BinaryExpr.Operator.AND
                    || binaryExpr.getOperator() == BinaryExpr.Operator.OR) {

                complexity++;

            }

        }

        return complexity;

    }

}