package com.softwaredna.analysis.architecture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArchitectureDiffAnalyzer {


    /*
     * =======================================================
     * Compare full architecture reports
     * =======================================================
     */

    public ArchitectureDiff analyze(
            ArchitectureReport previousReport,
            ArchitectureHealthReport previousHealth,
            ArchitectureReport currentReport,
            ArchitectureHealthReport currentHealth) {

        return analyzeData(
                previousReport.getArchitectureStyle(),
                previousHealth.getOverallScore(),
                buildDependencySet(previousReport),
                new HashSet<>(
                        previousReport.getViolations()
                ),

                currentReport.getArchitectureStyle(),
                currentHealth.getOverallScore(),
                buildDependencySet(currentReport),
                new HashSet<>(
                        currentReport.getViolations()
                )
        );

    }


    /*
     * =======================================================
     * Compare persisted snapshot data
     * =======================================================
     */

    public ArchitectureDiff analyze(
            ArchitectureSnapshotLoader.SnapshotData previous,
            ArchitectureSnapshotLoader.SnapshotData current) {

        return analyzeData(
                previous.getStyle(),
                previous.getHealth(),
                new HashSet<>(
                        previous.getDependencies()
                ),
                new HashSet<>(
                        previous.getAnomalies()
                ),

                current.getStyle(),
                current.getHealth(),
                new HashSet<>(
                        current.getDependencies()
                ),
                new HashSet<>(
                        current.getAnomalies()
                )
        );

    }


    /*
     * =======================================================
     * Common comparison logic
     * =======================================================
     */

    private ArchitectureDiff analyzeData(
            String previousStyle,
            double previousHealth,
            Set<String> previousDependencies,
            Set<String> previousAnomalies,

            String currentStyle,
            double currentHealth,
            Set<String> currentDependencies,
            Set<String> currentAnomalies) {


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
         * Build result
         * ===================================================
         */

        return new ArchitectureDiff(
                previousStyle,
                currentStyle,
                previousHealth,
                currentHealth,
                addedDependencies,
                removedDependencies,
                newAnomalies,
                resolvedAnomalies
        );

    }


    /*
     * =======================================================
     * Build dependency set from ArchitectureReport
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