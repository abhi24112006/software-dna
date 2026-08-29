package com.softwaredna.parser.language;

import java.io.IOException;

import com.softwaredna.language.Language;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.parser.RepositoryParser;

public class JavaParserAdapter
        implements LanguageParser {

    private final RepositoryParser parser;

    public JavaParserAdapter() {

        parser = new RepositoryParser();
    }

    @Override
    public Language getLanguage() {

        return Language.JAVA;
    }

    @Override
    public RepositoryModel parse(
            String repositoryPath)
            throws IOException {

        return parser.parseRepository(
                repositoryPath
        );
    }
}