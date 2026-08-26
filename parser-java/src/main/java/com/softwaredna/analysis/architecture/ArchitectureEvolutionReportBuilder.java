package com.softwaredna.analysis.architecture;

import java.util.List;

public class ArchitectureEvolutionReportBuilder {

    private final ArchitectureAnalyzer architectureAnalyzer;


    public ArchitectureEvolutionReportBuilder(
            ArchitectureAnalyzer architectureAnalyzer) {

        this.architectureAnalyzer =
                architectureAnalyzer;

    }


    public ArchitectureReport build(
            List<String> nodeIds) {

        return architectureAnalyzer.analyze(
                nodeIds
        );

    }

}