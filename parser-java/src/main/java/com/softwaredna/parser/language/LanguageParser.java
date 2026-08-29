package com.softwaredna.parser.language;

import java.io.IOException;

import com.softwaredna.language.Language;
import com.softwaredna.model.RepositoryModel;

public interface LanguageParser {

    Language getLanguage();

    RepositoryModel parse(String repositoryPath)
            throws IOException;
}