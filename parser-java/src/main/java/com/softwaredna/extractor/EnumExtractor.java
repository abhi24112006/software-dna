package com.softwaredna.extractor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.softwaredna.model.ParsedEnum;

import java.util.ArrayList;
import java.util.List;

public class EnumExtractor {

    public List<ParsedEnum> extractEnums(CompilationUnit cu) {

        List<ParsedEnum> enums = new ArrayList<>();

        for (EnumDeclaration declaration :
                cu.findAll(EnumDeclaration.class)) {

            ParsedEnum parsedEnum =
                    new ParsedEnum(
                            declaration.getNameAsString()
                    );

            enums.add(parsedEnum);

        }

        return enums;

    }

}