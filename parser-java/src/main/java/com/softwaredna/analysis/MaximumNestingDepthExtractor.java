package com.softwaredna.analysis;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;

public class MaximumNestingDepthExtractor {

    public int extract(MethodDeclaration method) {
        return calculateDepth(method, 0);
    }

    private int calculateDepth(Node node, int currentDepth) {

        int maxDepth = currentDepth;

        for (Node child : node.getChildNodes()) {

            int nextDepth = currentDepth;

            if (isNestingNode(child)) {
                nextDepth++;
            }

            maxDepth = Math.max(
                    maxDepth,
                    calculateDepth(child, nextDepth)
            );
        }

        return maxDepth;
    }

    private boolean isNestingNode(Node node) {

        return node instanceof IfStmt
                || node instanceof ForStmt
                || node instanceof ForEachStmt
                || node instanceof WhileStmt
                || node instanceof DoStmt
                || node instanceof SwitchStmt
                || node instanceof TryStmt
                || node instanceof CatchClause;
    }

}