package com.softwaredna.analysis.architecture;

import java.util.List;

public class ArchitectureTrendAnalyzer {

    public ArchitectureTrend analyze(
            List<ArchitectureDiff> diffs,
            double startingHealth,
            double currentHealth) {

        int totalChanges =
                diffs.size();

        int totalNewAnomalies = 0;

        int totalResolvedAnomalies = 0;

        int totalAddedDependencies = 0;

        int totalRemovedDependencies = 0;


        for (ArchitectureDiff diff :
                diffs) {

            totalNewAnomalies +=
                    diff.getNewAnomalies().size();

            totalResolvedAnomalies +=
                    diff.getResolvedAnomalies().size();

            totalAddedDependencies +=
                    diff.getAddedDependencies().size();

            totalRemovedDependencies +=
                    diff.getRemovedDependencies().size();

        }


        return new ArchitectureTrend(
                startingHealth,
                currentHealth,
                totalChanges,
                totalNewAnomalies,
                totalResolvedAnomalies,
                totalAddedDependencies,
                totalRemovedDependencies
        );

    }

}