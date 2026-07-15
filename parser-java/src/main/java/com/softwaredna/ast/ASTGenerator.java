package com.softwaredna.ast;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class ASTGenerator {

    static {

        ParserConfiguration configuration = new ParserConfiguration();

        configuration.setLanguageLevel(
                ParserConfiguration.LanguageLevel.JAVA_21
        );

        StaticJavaParser.setConfiguration(configuration);

    }

    public CompilationUnit generateAST(String sourceCode) {

        return StaticJavaParser.parse(sourceCode);

    }

}