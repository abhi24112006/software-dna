package com.softwaredna.extractor;

import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.softwaredna.model.ParsedParameter;

import java.util.ArrayList;
import java.util.List;

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

            parameters.add(parsedParameter);

        }

        return parameters;

    }

}