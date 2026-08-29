package com.softwaredna.language;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public class LanguageDetector {

    public Map<Language, Integer> detect(Path repositoryPath) {

        Map<Language, Integer> languageCounts =
                new EnumMap<>(Language.class);

        try (Stream<Path> files = Files.walk(repositoryPath)) {

            files.filter(Files::isRegularFile)
                 .forEach(file -> {

                     Language language =
                             detectFile(file);

                     languageCounts.merge(
                             language,
                             1,
                             Integer::sum
                     );
                 });

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to scan repository: "
                            + repositoryPath,
                    e
            );
        }

        return languageCounts;
    }

    private Language detectFile(Path file) {

        String fileName =
                file.getFileName()
                    .toString()
                    .toLowerCase();

        if (fileName.endsWith(".java")) {
            return Language.JAVA;
        }

        if (fileName.endsWith(".py")) {
            return Language.PYTHON;
        }

        if (fileName.endsWith(".js")) {
            return Language.JAVASCRIPT;
        }

        if (fileName.endsWith(".ts")) {
            return Language.TYPESCRIPT;
        }

        return Language.UNKNOWN;
    }
}