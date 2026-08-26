package com.softwaredna.analysis.architecture;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArchitectureReport {

    private final String architectureStyle;

    private final double confidence;

    private final Map<String, ArchitectureLayer> layers;

    private final List<ArchitectureEvidence> evidence;

    private final Set<String> violations;

    private final List<ArchitectureScore> scores;

    private final List<ArchitectureAnomaly> anomalies;


    public ArchitectureReport(
            String architectureStyle,
            double confidence,
            Map<String, ArchitectureLayer> layers,
            List<ArchitectureEvidence> evidence,
            Set<String> violations,
            List<ArchitectureScore> scores,
            List<ArchitectureAnomaly> anomalies) {

        this.architectureStyle =
                architectureStyle;

        this.confidence =
                confidence;

        this.layers =
                Collections.unmodifiableMap(
                        layers
                );

        this.evidence =
                Collections.unmodifiableList(
                        evidence
                );

        this.violations =
                Collections.unmodifiableSet(
                        violations
                );

        this.scores =
                Collections.unmodifiableList(
                        scores
                );

        this.anomalies =
                Collections.unmodifiableList(
                        anomalies
                );

    }


    public String getArchitectureStyle() {

        return architectureStyle;

    }


    public double getConfidence() {

        return confidence;

    }


    public Map<String, ArchitectureLayer>
    getLayers() {

        return layers;

    }


    public List<ArchitectureEvidence>
    getEvidence() {

        return evidence;

    }


    public Set<String>
    getViolations() {

        return violations;

    }


    public List<ArchitectureScore>
    getScores() {

        return scores;

    }


    public List<ArchitectureAnomaly>
    getAnomalies() {

        return anomalies;

    }


    public void print() {

        System.out.println();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Architecture Recovery"
        );

        System.out.println(
                "======================================"
        );


        /*
         * ===================================================
         * Selected Architecture
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "Architecture Style : "
                        + architectureStyle
        );

        System.out.printf(
                "Confidence          : %.2f%n",
                confidence
        );


        /*
         * ===================================================
         * Architecture Candidates
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "Architecture Candidates:"
        );


        for (ArchitectureScore score :
                scores) {

            System.out.printf(
                    "  %-15s : %.2f%n",
                    score.getArchitectureStyle(),
                    score.getScore()
            );

            System.out.println(
                    "    -> "
                            + score.getExplanation()
            );

        }


        /*
         * ===================================================
         * Layers
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "Layers:"
        );


        for (Map.Entry<String, ArchitectureLayer>
                entry : layers.entrySet()) {

            if (entry.getValue() ==
                    ArchitectureLayer.UNKNOWN) {

                continue;

            }

            System.out.println(
                    "  "
                            + entry.getValue()
                            + " -> "
                            + entry.getKey()
            );

        }


        /*
         * ===================================================
         * Architecture Evidence
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "Architecture Evidence:"
        );


        for (ArchitectureEvidence item :
                evidence) {

            System.out.println(
                    "  [OK] "
                            + item
            );

        }


        /*
         * ===================================================
         * Architecture Validation
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "Architecture Validation:"
        );


        for (ArchitectureEvidence item :
                evidence) {

            String explanation =
                    item.getExplanation();


            if (explanation.contains(
                    "valid layered dependency")
                    || explanation.contains(
                    "supports MVC")) {

                System.out.println(
                        "  [OK] "
                                + item.getSource()
                                + " -> "
                                + item.getTarget()
                );

            }

        }


        /*
         * ===================================================
         * Architecture Anomalies
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "Architecture Anomalies:"
        );


        if (anomalies.isEmpty()) {

            System.out.println(
                    "  None"
            );

        }
        else {

            for (ArchitectureAnomaly anomaly :
                    anomalies) {

                System.out.println();

                System.out.println(
                        "  ["
                                + anomaly.getSeverity()
                                + "] "
                                + anomaly.getSource()
                                + " --"
                                + anomaly.getRelationship()
                                + "--> "
                                + anomaly.getTarget()
                );

                System.out.println(
                        "    "
                                + anomaly.getSourceLayer()
                                + " -> "
                                + anomaly.getTargetLayer()
                );

                System.out.println(
                        "    Reason: "
                                + anomaly.getDescription()
                );

            }

        }


        /*
         * ===================================================
         * Conclusion
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "Conclusion:"
        );


        if ("LAYERED".equals(
                architectureStyle)) {

            if (anomalies.isEmpty()) {

                System.out.println(
                        "  The repository follows "
                                + "a layered architecture."
                );

                System.out.println(
                        "  Dependencies follow "
                                + "recognized architectural "
                                + "layer boundaries."
                );

            }
            else {

                System.out.println(
                        "  The repository resembles "
                                + "a layered architecture."
                );

                System.out.println(
                        "  However, architectural "
                                + "anomalies were detected."
                );

            }

        }
        else if ("MVC".equals(
                architectureStyle)) {

            if (anomalies.isEmpty()) {

                System.out.println(
                        "  The repository follows "
                                + "an MVC architecture."
                );

                System.out.println(
                        "  Controller, Model, and View "
                                + "components were detected with "
                                + "supporting graph evidence."
                );

            }
            else {

                System.out.println(
                        "  The repository resembles "
                                + "an MVC architecture."
                );

                System.out.println(
                        "  However, architectural "
                                + "anomalies were detected."
                );

            }

        }
        else if ("MICROSERVICES".equals(
                architectureStyle)) {

            if (anomalies.isEmpty()) {

                System.out.println(
                        "  The repository exhibits "
                                + "a microservices architecture."
                );

                System.out.println(
                        "  Multiple independent service "
                                + "boundaries were detected."
                );

            }
            else {

                System.out.println(
                        "  The repository resembles "
                                + "a microservices architecture."
                );

                System.out.println(
                        "  However, architectural "
                                + "anomalies were detected."
                );

            }

        }
        else {

            System.out.println(
                    "  No recognized architecture "
                            + "style was detected."
            );

        }


        System.out.println();

    }

}