package com.softwaredna.extractor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.softwaredna.model.ParsedConstructor;

import java.util.ArrayList;
import java.util.List;

public class ConstructorExtractor {

    public List<ParsedConstructor> extractConstructors(
            ClassOrInterfaceDeclaration classDeclaration) {

        List<ParsedConstructor> constructors = new ArrayList<>();

        for (ConstructorDeclaration constructor :
                classDeclaration.getConstructors()) {

            ParsedConstructor parsedConstructor =
                    new ParsedConstructor(

                            constructor.getNameAsString(),

                            constructor.getParameters().size()

                    );

            constructors.add(parsedConstructor);

        }

        return constructors;

    }

}