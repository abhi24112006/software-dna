package com.softwaredna.language;

import java.util.Map;

public class LanguageReport {

    private final Map<Language, Integer> languageCounts;

    public LanguageReport(Map<Language, Integer> languageCounts) {

        this.languageCounts = languageCounts;
    }

    public Map<Language, Integer> getLanguageCounts() {

        return languageCounts;
    }

    public void print() {

        System.out.println();
        System.out.println("======================================");
        System.out.println("Language Detection");
        System.out.println("======================================");

        if (languageCounts.isEmpty()) {

            System.out.println("No source files detected.");
            return;
        }

        System.out.println();
        System.out.println("Detected Languages:");

        for (Map.Entry<Language, Integer> entry :
                languageCounts.entrySet()) {

            if (entry.getKey() != Language.UNKNOWN) {

                System.out.println(
                        "  "
                        + entry.getKey()
                        + " : "
                        + entry.getValue()
                        + " files"
                );
            }
        }

        System.out.println();

        Language primaryLanguage =
                getPrimaryLanguage();

        System.out.println(
                "Primary Language : "
                        + primaryLanguage
        );

        System.out.println();
    }

    public Language getPrimaryLanguage() {

        Language primary = Language.UNKNOWN;

        int highestCount = 0;

        for (Map.Entry<Language, Integer> entry :
                languageCounts.entrySet()) {

            if (entry.getKey() == Language.UNKNOWN) {
                continue;
            }

            if (entry.getValue() > highestCount) {

                highestCount = entry.getValue();

                primary = entry.getKey();
            }
        }

        return primary;
    }
}