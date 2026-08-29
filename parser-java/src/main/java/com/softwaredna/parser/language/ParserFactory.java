package com.softwaredna.parser.language;

import com.softwaredna.language.Language;

public class ParserFactory {

    public static LanguageParser getParser(
            Language language) {

        if (language == null) {

            throw new IllegalArgumentException(
                    "Language cannot be null."
            );
        }

        switch (language) {

            case JAVA:
                return new JavaParserAdapter();

            case PYTHON:
                return new PythonParserAdapter();

            case JAVASCRIPT:
                return new JavaScriptParserAdapter();

            default:
                throw new UnsupportedOperationException(
                        "Parser not yet implemented for: "
                                + language
                );
        }
    }
}