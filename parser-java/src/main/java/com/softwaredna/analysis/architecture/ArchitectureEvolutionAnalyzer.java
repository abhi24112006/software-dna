package com.softwaredna.analysis.architecture;

public class ArchitectureEvolutionAnalyzer {

    private final ArchitectureDiffAnalyzer diffAnalyzer;

    private final ArchitectureHealthAnalyzer healthAnalyzer;


    public ArchitectureEvolutionAnalyzer() {

        diffAnalyzer =
                new ArchitectureDiffAnalyzer();

        healthAnalyzer =
                new ArchitectureHealthAnalyzer();

    }


    public ArchitectureDiff analyze(
            ArchitectureReport previousReport,
            ArchitectureReport currentReport) {

        ArchitectureHealthReport previousHealth =
                healthAnalyzer.analyze(
                        previousReport
                );

        ArchitectureHealthReport currentHealth =
                healthAnalyzer.analyze(
                        currentReport
                );


        return diffAnalyzer.analyze(
                previousReport,
                previousHealth,
                currentReport,
                currentHealth
        );

    }

}