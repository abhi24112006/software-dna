package com.softwaredna.analysis.architecture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArchitectureDiffAnalyzer {


    public ArchitectureDiff analyze(
            ArchitectureReport previousReport,
            ArchitectureHealthReport previousHealth,
            ArchitectureReport currentReport,
            ArchitectureHealthReport currentHealth) {


        /*
         * ===================================================
         * Architecture Style
         * ===================================================
         */

        String previousStyle =
                previousReport.getArchitectureStyle();

        String currentStyle =
                currentReport.getArchitectureStyle();


        /*
         * ===================================================
         * Dependency Sets
         * ===================================================
         */

        Set<String> previousDependencies =
                buildDependencySet(
                        previousReport
                );


        Set<String> currentDependencies =
                buildDependencySet(
                        currentReport
                );


        /*
         * ===================================================
         * Added Dependencies
         * ===================================================
         */

        List<String> addedDependencies =
                new ArrayList<>();


        for (String dependency :
                currentDependencies) {

            if (!previousDependencies.contains(
                    dependency)) {

                addedDependencies.add(
                        dependency
                );

            }

        }


        /*
         * ===================================================
         * Removed Dependencies
         * ===================================================
         */

        List<String> removedDependencies =
                new ArrayList<>();


        for (String dependency :
                previousDependencies) {

            if (!currentDependencies.contains(
                    dependency)) {

                removedDependencies.add(
                        dependency
                );

            }

        }


        /*
         * ===================================================
         * Anomaly Sets
         * ===================================================
         */

        Set<String> previousAnomalies =
                new HashSet<>(
                        previousReport.getViolations()
                );


        Set<String> currentAnomalies =
                new HashSet<>(
                        currentReport.getViolations()
                );


        /*
         * ===================================================
         * New Anomalies
         * ===================================================
         */

        List<String> newAnomalies =
                new ArrayList<>();


        for (String anomaly :
                currentAnomalies) {

            if (!previousAnomalies.contains(
                    anomaly)) {

                newAnomalies.add(
                        anomaly
                );

            }

        }


        /*
         * ===================================================
         * Resolved Anomalies
         * ===================================================
         */

        List<String> resolvedAnomalies =
                new ArrayList<>();


        for (String anomaly :
                previousAnomalies) {

            if (!currentAnomalies.contains(
                    anomaly)) {

                resolvedAnomalies.add(
                        anomaly
                );

            }

        }


        /*
         * ===================================================
         * Health
         * ===================================================
         */

        double previousHealthScore =
                previousHealth.getOverallScore();


        double currentHealthScore =
                currentHealth.getOverallScore();


        /*
         * ===================================================
         * Build Diff
         * ===================================================
         */

        return new ArchitectureDiff(
                previousStyle,
                currentStyle,
                previousHealthScore,
                currentHealthScore,
                addedDependencies,
                removedDependencies,
                newAnomalies,
                resolvedAnomalies
        );

    }


    /*
     * =======================================================
     * Dependency Set
     * =======================================================
     */

    private Set<String> buildDependencySet(
            ArchitectureReport report) {

        Set<String> dependencies =
                new HashSet<>();


        for (ArchitectureEvidence evidence :
                report.getEvidence()) {

            dependencies.add(
                    evidence.getSource()
                            + " --"
                            + evidence.getRelationship()
                            + "--> "
                            + evidence.getTarget()
            );

        }


        return dependencies;

    }

}