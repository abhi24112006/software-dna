package com.softwaredna.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.softwaredna.analysis.repository.RepositoryAnalyzer;
import com.softwaredna.language.Language;
import com.softwaredna.language.LanguageDetector;
import com.softwaredna.language.LanguageReport;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.Relationship;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.parser.language.LanguageParser;
import com.softwaredna.parser.language.ParserFactory;

public class MultiLanguageRepositoryParser {

    private final LanguageDetector languageDetector;
        private final RepositoryAnalyzer repositoryAnalyzer;

    public MultiLanguageRepositoryParser() {
        languageDetector = new LanguageDetector();
                repositoryAnalyzer = new RepositoryAnalyzer();
    }

    /**
     * Parses all supported languages found in a repository
     * and combines their parsed files and relationships into
     * one RepositoryModel.
     *
     * <p>The existing language-specific adapters remain
     * responsible for language-specific parsing and relationship
     * extraction.</p>
     *
     * @param repositoryPath path to the repository
     * @return unified repository model
     * @throws IOException if the repository cannot be read
     */
    public RepositoryModel parseRepository(
            String repositoryPath)
            throws IOException {

        if (repositoryPath == null
                || repositoryPath.isBlank()) {

            throw new IllegalArgumentException(
                    "Repository path cannot be null or blank."
            );
        }

        Path root = Path.of(repositoryPath);

        if (!Files.exists(root)) {
            throw new IllegalArgumentException(
                    "Repository path does not exist: "
                            + repositoryPath
            );
        }

        if (!Files.isDirectory(root)) {
            throw new IllegalArgumentException(
                    "Repository path is not a directory: "
                            + repositoryPath
            );
        }

        Map<Language, Integer> languageCounts =
                languageDetector.detect(root);

        LanguageReport languageReport =
                new LanguageReport(languageCounts);

        RepositoryModel unifiedRepository =
                new RepositoryModel();

        Path fileName = root.getFileName();

        unifiedRepository.setRepositoryName(
                fileName == null
                        ? root.toString()
                        : fileName.toString()
        );

        /*
         * Parse each supported language independently.
         *
         * The order is deterministic so that repeated
         * parsing produces predictable results.
         */
        Set<Language> supportedLanguages =
                EnumSet.of(
                        Language.JAVA,
                        Language.PYTHON,
                        Language.JAVASCRIPT,
                        Language.TYPESCRIPT
                );

        List<Language> detectedLanguages =
                new ArrayList<>();

        for (Language language :
                supportedLanguages) {

            Integer count =
                    languageCounts.get(language);

            if (count != null && count > 0) {
                detectedLanguages.add(language);
            }
        }

        /*
         * JavaScriptParserAdapter already supports
         * TypeScript files, so JAVASCRIPT and TYPESCRIPT
         * must not cause the JavaScript parser to run twice.
         */
        if (detectedLanguages.contains(
                Language.JAVASCRIPT)
                && detectedLanguages.contains(
                        Language.TYPESCRIPT)) {

            detectedLanguages.remove(
                    Language.TYPESCRIPT
            );
        }

        detectedLanguages.sort(
                Comparator.comparingInt(
                        Enum::ordinal
                )
        );

        for (Language language :
                detectedLanguages) {

            LanguageParser parser =
                    ParserFactory.getParser(
                            language
                    );

            RepositoryModel parsedRepository =
                    parser.parse(
                            repositoryPath
                    );

            mergeRepository(
                    unifiedRepository,
                    parsedRepository
            );
        }

        repositoryAnalyzer.analyze(unifiedRepository);

        return unifiedRepository;
    }

    /**
     * Returns the language detection report for a repository.
     *
     * @param repositoryPath path to the repository
     * @return language report
     */
    public LanguageReport detectLanguages(
            String repositoryPath) {

        if (repositoryPath == null
                || repositoryPath.isBlank()) {

            throw new IllegalArgumentException(
                    "Repository path cannot be null or blank."
            );
        }

        Path root = Path.of(repositoryPath);

        if (!Files.exists(root)) {
            throw new IllegalArgumentException(
                    "Repository path does not exist: "
                            + repositoryPath
            );
        }

        if (!Files.isDirectory(root)) {
            throw new IllegalArgumentException(
                    "Repository path is not a directory: "
                            + repositoryPath
            );
        }

        return new LanguageReport(
                languageDetector.detect(root)
        );
    }

    private void mergeRepository(
            RepositoryModel target,
            RepositoryModel source) {

        if (source == null) {
            return;
        }

        for (ParsedFile file :
                source.getFiles()) {

            target.getFiles().add(file);
        }

        for (Relationship relationship :
                source.getRelationships()) {

            target.getRelationships().add(
                    relationship
            );
        }

        for (Object entity :
                source.getEntityRegistry()
                        .getAllParsedEntities()) {

            /*
             * EntityRegistry does not currently expose
             * a generic registration method, so parsed
             * entities are reconstructed through the
             * target registry's typed registration API.
             */
            registerParsedEntity(
                    target,
                    entity
            );
        }

        source.getEntityRegistry()
                .getAllEntityReferences()
                .forEach(
                        target.getEntityRegistry()
                                ::registerEntityReference
                );
    }

    private void registerParsedEntity(
            RepositoryModel repository,
            Object entity) {

        if (entity instanceof com.softwaredna.model.ParsedClass) {

            repository.getEntityRegistry()
                    .registerClass(
                            (com.softwaredna.model.ParsedClass)
                                    entity
                    );

        } else if (entity instanceof com.softwaredna.model.ParsedInterface) {

            repository.getEntityRegistry()
                    .registerInterface(
                            (com.softwaredna.model.ParsedInterface)
                                    entity
                    );

        } else if (entity instanceof com.softwaredna.model.ParsedEnum) {

            repository.getEntityRegistry()
                    .registerEnum(
                            (com.softwaredna.model.ParsedEnum)
                                    entity
                    );

        } else if (entity instanceof com.softwaredna.model.ParsedRecord) {

            repository.getEntityRegistry()
                    .registerRecord(
                            (com.softwaredna.model.ParsedRecord)
                                    entity
                    );

        } else if (entity instanceof com.softwaredna.model.ParsedField) {

            repository.getEntityRegistry()
                    .registerField(
                            (com.softwaredna.model.ParsedField)
                                    entity
                    );

        } else if (entity instanceof com.softwaredna.model.ParsedMethod) {

            repository.getEntityRegistry()
                    .registerMethod(
                            (com.softwaredna.model.ParsedMethod)
                                    entity
                    );

        } else if (entity instanceof com.softwaredna.model.ParsedConstructor) {

            repository.getEntityRegistry()
                    .registerConstructor(
                            (com.softwaredna.model.ParsedConstructor)
                                    entity
                    );

        } else if (entity instanceof com.softwaredna.model.ParsedParameter) {

            repository.getEntityRegistry()
                    .registerParameter(
                            (com.softwaredna.model.ParsedParameter)
                                    entity
                    );
        }
    }
}