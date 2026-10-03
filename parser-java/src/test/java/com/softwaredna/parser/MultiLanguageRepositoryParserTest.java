package com.softwaredna.parser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.softwaredna.language.Language;
import com.softwaredna.language.LanguageReport;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.RepositoryModel;

class MultiLanguageRepositoryParserTest {

    @Test
    void detectsMultipleLanguages() throws Exception {

        Path repository =
                Files.createTempDirectory(
                        "software-dna-multi-language"
                );

        Files.writeString(
                repository.resolve("Example.java"),
                """
                public class Example {
                }
                """
        );

        Files.writeString(
                repository.resolve("example.py"),
                """
                class ExamplePython:
                    pass
                """
        );

        Files.writeString(
                repository.resolve("example.js"),
                """
                class ExampleJavaScript {
                }
                """
        );

        MultiLanguageRepositoryParser parser =
                new MultiLanguageRepositoryParser();

        LanguageReport report =
                parser.detectLanguages(
                        repository.toString()
                );

        Map<Language, Integer> counts =
                report.getLanguageCounts();

        assertEquals(
                1,
                counts.get(Language.JAVA)
        );

        assertEquals(
                1,
                counts.get(Language.PYTHON)
        );

        assertEquals(
                1,
                counts.get(Language.JAVASCRIPT)
        );
    }

    @Test
    void parsesMultipleLanguagesIntoOneRepository()
            throws Exception {

        Path repository =
                Files.createTempDirectory(
                        "software-dna-multi-language"
                );

        Files.writeString(
                repository.resolve("Example.java"),
                """
                public class Example {
                    public void hello() {
                    }
                }
                """
        );

        Files.writeString(
                repository.resolve("example.py"),
                """
                class ExamplePython:
                    def hello(self):
                        return 1
                """
        );

        Files.writeString(
                repository.resolve("example.js"),
                """
                class ExampleJavaScript {
                    hello() {
                        return 1;
                    }
                }
                """
        );

        MultiLanguageRepositoryParser parser =
                new MultiLanguageRepositoryParser();

        RepositoryModel repositoryModel =
                parser.parseRepository(
                        repository.toString()
                );

        assertNotNull(repositoryModel);
        assertNotNull(repositoryModel.getMetrics());

        assertEquals(
                3,
                repositoryModel.getMetrics().getTotalFiles()
        );

        assertEquals(
                3,
                repositoryModel.getMetrics().getTotalClasses()
        );

        assertEquals(
                3,
                repositoryModel.getFiles().size()
        );

        assertTrue(
                repositoryModel.getFiles()
                        .stream()
                        .anyMatch(
                                file ->
                                        !file.getClasses()
                                                .isEmpty()
                        )
        );
    }

    @Test
    void supportsTypeScriptThroughJavaScriptParser()
            throws Exception {

        Path repository =
                Files.createTempDirectory(
                        "software-dna-typescript"
                );

        Files.writeString(
                repository.resolve("Example.ts"),
                """
                class Example {
                    hello(): number {
                        return 1;
                    }
                }
                """
        );

        MultiLanguageRepositoryParser parser =
                new MultiLanguageRepositoryParser();

        RepositoryModel repositoryModel =
                parser.parseRepository(
                        repository.toString()
                );

        assertNotNull(repositoryModel);

        assertEquals(
                1,
                repositoryModel.getFiles().size()
        );
    }

    @Test
    void usesRepositoryRelativePackageForNestedJavaScriptFiles()
            throws Exception {

        Path repository =
                Files.createTempDirectory(
                        "software-dna-javascript"
                );

        Path modelDirectory =
                Files.createDirectories(
                        repository.resolve("model")
                );

        Files.writeString(
                modelDirectory.resolve("User.js"),
                """
                class User {
                    getName() {
                        return "Abhishek";
                    }
                }
                """
        );

        MultiLanguageRepositoryParser parser =
                new MultiLanguageRepositoryParser();

        RepositoryModel repositoryModel =
                parser.parseRepository(
                        repository.toString()
                );

        assertNotNull(repositoryModel);

        ParsedClass userClass =
                repositoryModel.getFiles()
                        .stream()
                        .flatMap(
                                file ->
                                        file.getClasses()
                                                .stream()
                        )
                        .filter(
                                parsedClass ->
                                        "User".equals(
                                                parsedClass.getName()
                                        )
                        )
                        .findFirst()
                        .orElse(null);

        assertNotNull(userClass);

        assertEquals(
                "model",
                userClass.getPackageName()
        );

        assertEquals(
                "model.User",
                userClass.getId()
        );

        assertFalse(
                userClass.getId()
                        .contains(
                                repository.toAbsolutePath()
                                        .toString()
                        )
        );
    }
}
