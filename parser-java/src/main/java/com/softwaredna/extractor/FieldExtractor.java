package com.softwaredna.extractor;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.CompilationUnit;
import com.softwaredna.model.ParsedField;

import java.util.ArrayList;
import java.util.List;

public class FieldExtractor {

    public List<ParsedField> extractFields(CompilationUnit cu) {

        List<ParsedField> fields = new ArrayList<>();

        for (FieldDeclaration field : cu.findAll(FieldDeclaration.class)) {

            for (VariableDeclarator variable : field.getVariables()) {

                ParsedField parsedField = new ParsedField(
                        variable.getNameAsString(),
                        variable.getType().asString()
                );

                fields.add(parsedField);

            }

        }

        return fields;

    }

}