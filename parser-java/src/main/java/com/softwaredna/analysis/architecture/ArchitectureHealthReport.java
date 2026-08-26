package com.softwaredna.analysis.architecture;

import java.util.Collections;
import java.util.List;

public class ArchitectureHealthReport {

    private final double overallScore;

    private final double structuralQuality;

    private final double dependencyQuality;

    private final double layerCompliance;

    private final double anomalyImpact;

    private final String healthRating;

    private final List<String> criticalIssues;

    private final List<String> recommendations;


    public ArchitectureHealthReport(
            double overallScore,
            double structuralQuality,
            double dependencyQuality,
            double layerCompliance,
            double anomalyImpact,
            String healthRating,
            List<String> criticalIssues,
            List<String> recommendations) {

        this.overallScore =
                overallScore;

        this.structuralQuality =
                structuralQuality;

        this.dependencyQuality =
                dependencyQuality;

        this.layerCompliance =
                layerCompliance;

        this.anomalyImpact =
                anomalyImpact;

        this.healthRating =
                healthRating;

        this.criticalIssues =
                Collections.unmodifiableList(
                        criticalIssues
                );

        this.recommendations =
                Collections.unmodifiableList(
                        recommendations
                );

    }


    public double getOverallScore() {

        return overallScore;

    }


    public double getStructuralQuality() {

        return structuralQuality;

    }


    public double getDependencyQuality() {

        return dependencyQuality;

    }


    public double getLayerCompliance() {

        return layerCompliance;

    }


    public double getAnomalyImpact() {

        return anomalyImpact;

    }


    public String getHealthRating() {

        return healthRating;

    }


    public List<String>
    getCriticalIssues() {

        return criticalIssues;

    }


    public List<String>
    getRecommendations() {

        return recommendations;

    }


    public void print() {

        System.out.println();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Architecture Health"
        );

        System.out.println(
                "======================================"
        );

        System.out.println();


        System.out.printf(
                "Architecture Score : %.0f / 100%n",
                overallScore
        );


        System.out.printf(
                "Structural Quality : %.0f / 100%n",
                structuralQuality
        );


        System.out.printf(
                "Dependency Quality : %.0f / 100%n",
                dependencyQuality
        );


        System.out.printf(
                "Layer Compliance   : %.0f / 100%n",
                layerCompliance
        );


        System.out.printf(
                "Anomaly Impact      : -%.0f%n",
                anomalyImpact
        );


        System.out.println();

        System.out.println(
                "Health Rating       : "
                        + healthRating
        );


        System.out.println();

        System.out.println(
                "Critical Issues:"
        );


        if (criticalIssues.isEmpty()) {

            System.out.println(
                    "  None"
            );

        }
        else {

            for (String issue :
                    criticalIssues) {

                System.out.println(
                        "  [HIGH] "
                                + issue
                );

            }

        }


        System.out.println();

        System.out.println(
                "Recommendations:"
        );


        if (recommendations.isEmpty()) {

            System.out.println(
                    "  None"
            );

        }
        else {

            for (String recommendation :
                    recommendations) {

                System.out.println(
                        "  - "
                                + recommendation
                );

            }

        }


        System.out.println();

    }

}