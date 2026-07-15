package com.softwaredna.extractor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.softwaredna.model.ParsedField;

import java.util.ArrayList;
import java.util.List;

public class FieldExtractor {

    public List<ParsedField> extractFields(
            ClassOrInterfaceDeclaration classDeclaration) {

        List<ParsedField> fields = new ArrayList<>();

        for (FieldDeclaration field :
                classDeclaration.getFields()) {

            for (VariableDeclarator variable :
                    field.getVariables()) {

                ParsedField parsedField =
                        new ParsedField(

                                variable.getNameAsString(),

                                variable.getType().asString()

                        );

                fields.add(parsedField);

            }

        }

        return fields;

    }

}