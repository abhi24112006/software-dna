package com.softwaredna.analysis.architecture;

public class ArchitectureSnapshotComparator {

    private final ArchitectureEvolutionAnalyzer
            evolutionAnalyzer;


    public ArchitectureSnapshotComparator() {

        evolutionAnalyzer =
                new ArchitectureEvolutionAnalyzer();

    }


    public ArchitectureDiff compare(
            ArchitectureSnapshot previous,
            ArchitectureSnapshot current) {

        return evolutionAnalyzer.analyze(
                previous.getReport(),
                current.getReport()
        );

    }

}