package com.softwaredna.extractor;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.softwaredna.model.ParsedParameter;

public class ParameterExtractor {

    public List<ParsedParameter> extractParameters(
            CallableDeclaration<?> callable) {

        List<ParsedParameter> parameters =
                new ArrayList<>();

        for (Parameter parameter : callable.getParameters()) {

            ParsedParameter parsedParameter =
                    new ParsedParameter();

            parsedParameter.setName(
                    parameter.getNameAsString());

            parsedParameter.setType(
                    parameter.getType().asString());

            /*
             * Capture the source line where the
             * parameter is declared.
             */
            if (parameter.getBegin().isPresent()) {

                parsedParameter.setSourceLineNumber(
                        parameter.getBegin().get().line
                );
            }

            parameters.add(parsedParameter);
        }

        return parameters;
    }
}