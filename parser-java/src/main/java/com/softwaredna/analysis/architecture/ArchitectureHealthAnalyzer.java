package com.softwaredna.analysis.architecture;

import java.util.ArrayList;
import java.util.List;

public class ArchitectureHealthAnalyzer {

    public ArchitectureHealthReport analyze(
            ArchitectureReport report) {

        /*
         * ===================================================
         * Structural Quality
         * ===================================================
         */

        double structuralQuality =
                report.getConfidence() * 100.0;


        /*
         * ===================================================
         * Dependency Quality
         * ===================================================
         */

        double dependencyQuality =
                100.0;

        dependencyQuality -=
                report.getAnomalies().size() * 15.0;

        dependencyQuality =
                clamp(
                        dependencyQuality,
                        0,
                        100
                );


        /*
         * ===================================================
         * Layer Compliance
         * ===================================================
         */

        int validRelationships =
                report.getEvidence().size();

        int anomalyCount =
                report.getAnomalies().size();

        double layerCompliance;

        if (validRelationships == 0) {

            layerCompliance = 0;

        }
        else {

            layerCompliance =
                    (
                            validRelationships * 100.0
                    )
                    /
                    (
                            validRelationships
                                    + anomalyCount
                    );

        }

        layerCompliance =
                clamp(
                        layerCompliance,
                        0,
                        100
                );


        /*
         * ===================================================
         * Anomaly Impact
         * ===================================================
         */

        double anomalyImpact = 0.0;

        for (ArchitectureAnomaly anomaly :
                report.getAnomalies()) {

            if ("HIGH".equals(
                    anomaly.getSeverity())) {

                anomalyImpact += 10.0;

            }
            else {

                anomalyImpact += 5.0;

            }

        }

        anomalyImpact =
                Math.min(
                        anomalyImpact,
                        50.0
                );


        /*
         * ===================================================
         * Overall Score
         * ===================================================
         */

        double overallScore =
                (
                        structuralQuality * 0.30
                                +
                        dependencyQuality * 0.30
                                +
                        layerCompliance * 0.40
                );

        overallScore -=
                anomalyImpact;

        overallScore =
                clamp(
                        overallScore,
                        0,
                        100
                );


        /*
         * ===================================================
         * Health Rating
         * ===================================================
         */

        String healthRating =
                determineHealthRating(
                        overallScore
                );


        /*
         * ===================================================
         * Critical Issues
         * ===================================================
         */

        List<String> criticalIssues =
                new ArrayList<>();

        for (ArchitectureAnomaly anomaly :
                report.getAnomalies()) {

            criticalIssues.add(
                    anomaly.getSource()
                            + " --"
                            + anomaly.getRelationship()
                            + "--> "
                            + anomaly.getTarget()
            );

        }


        /*
         * ===================================================
         * Recommendations
         * ===================================================
         */

        List<String> recommendations =
                generateRecommendations(
                        report
                );


        return new ArchitectureHealthReport(
                overallScore,
                structuralQuality,
                dependencyQuality,
                layerCompliance,
                anomalyImpact,
                healthRating,
                criticalIssues,
                recommendations
        );

    }


    /*
     * ===================================================
     * Health Rating
     * ===================================================
     */

    private String determineHealthRating(
            double score) {

        if (score >= 90) {

            return "EXCELLENT";

        }

        if (score >= 75) {

            return "GOOD";

        }

        if (score >= 60) {

            return "MODERATE";

        }

        if (score >= 40) {

            return "POOR";

        }

        return "CRITICAL";

    }


    /*
     * ===================================================
     * Recommendations
     * ===================================================
     */

    private List<String>
    generateRecommendations(
            ArchitectureReport report) {

        List<String> recommendations =
                new ArrayList<>();


        for (ArchitectureAnomaly anomaly :
                report.getAnomalies()) {

            ArchitectureLayer source =
                    anomaly.getSourceLayer();

            ArchitectureLayer target =
                    anomaly.getTargetLayer();


            if (source ==
                    ArchitectureLayer.REPOSITORY
                    && target ==
                    ArchitectureLayer.SERVICE) {

                recommendations.add(
                        "Reverse the dependency direction: "
                                + "Repository should not depend "
                                + "on Service."
                );

            }

            else if (source ==
                    ArchitectureLayer.REPOSITORY
                    && target ==
                    ArchitectureLayer.CONTROLLER) {

                recommendations.add(
                        "Remove Repository dependency on "
                                + "Controller."
                );

            }

            else if (source ==
                    ArchitectureLayer.SERVICE
                    && target ==
                    ArchitectureLayer.CONTROLLER) {

                recommendations.add(
                        "Remove Service dependency on "
                                + "Controller."
                );

            }

            else if (source ==
                    ArchitectureLayer.VIEW
                    && target ==
                    ArchitectureLayer.REPOSITORY) {

                recommendations.add(
                        "Move persistence access out of "
                                + "the View layer."
                );

            }

            else if (source ==
                    ArchitectureLayer.VIEW
                    && target ==
                    ArchitectureLayer.SERVICE) {

                recommendations.add(
                        "Avoid direct View-to-Service "
                                + "dependencies."
                );

            }

        }


        if (recommendations.isEmpty()
                && report.getAnomalies().isEmpty()) {

            recommendations.add(
                    "Architecture currently follows "
                            + "recognized dependency boundaries."
            );

        }


        return recommendations;

    }


    /*
     * ===================================================
     * Utility
     * ===================================================
     */

    private double clamp(
            double value,
            double min,
            double max) {

        return Math.max(
                min,
                Math.min(
                        value,
                        max
                )
        );

    }

}