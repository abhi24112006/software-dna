package com.softwaredna.extractor;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.softwaredna.model.ParsedField;

public class FieldExtractor {

    public List<ParsedField> extractFields(
            ClassOrInterfaceDeclaration classDeclaration) {

        List<ParsedField> fields =
                new ArrayList<>();


        for (FieldDeclaration field :
                classDeclaration.getFields()) {

            for (VariableDeclarator variable :
                    field.getVariables()) {

                ParsedField parsedField =
                        new ParsedField(
                                variable.getNameAsString(),
                                variable.getType().asString()
                        );


                /*
                 * Capture the source line of the
                 * individual field variable.
                 *
                 * This keeps source-location metadata
                 * inside the language-independent model.
                 */
                if (variable.getBegin().isPresent()) {

                    parsedField.setSourceLineNumber(
                            variable.getBegin()
                                    .get()
                                    .line
                    );
                }


                fields.add(parsedField);

            }

        }


        return fields;

    }

}