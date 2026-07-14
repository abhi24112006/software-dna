package com.softwaredna.ast;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class ASTGenerator {

    public CompilationUnit generateAST(String sourceCode) {

        return StaticJavaParser.parse(sourceCode);

    }

}