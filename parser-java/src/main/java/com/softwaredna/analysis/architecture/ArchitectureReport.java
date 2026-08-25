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


    public ArchitectureReport(
            String architectureStyle,
            double confidence,
            Map<String, ArchitectureLayer> layers,
            List<ArchitectureEvidence> evidence,
            Set<String> violations) {

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

    }


    /*
     * =======================================================
     * Getters
     * =======================================================
     */

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


    public Set<String> getViolations() {

        return violations;

    }


    /*
     * =======================================================
     * Print Report
     * =======================================================
     */

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
         * ---------------------------------------------------
         * Architecture Style
         * ---------------------------------------------------
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
         * ---------------------------------------------------
         * Layers
         * ---------------------------------------------------
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
         * ---------------------------------------------------
         * Evidence
         * ---------------------------------------------------
         */

        System.out.println();

        System.out.println(
                "Architecture Evidence:"
        );


        if (evidence.isEmpty()) {

            System.out.println(
                    "  None"
            );

        }

        else {

            for (ArchitectureEvidence item :
                    evidence) {

                System.out.println(
                        "  [OK] "
                        + item
                );

            }

        }


        /*
         * ---------------------------------------------------
         * Validation
         * ---------------------------------------------------
         */

        System.out.println();

        System.out.println(
                "Architecture Validation:"
        );


        if (evidence.isEmpty()) {

            System.out.println(
                    "  No valid architectural "
                    + "dependencies detected."
            );

        }

        else {

            for (ArchitectureEvidence item :
                    evidence) {

                System.out.println(
                        "  [OK] "
                        + item.getSource()
                        + " -> "
                        + item.getTarget()
                );

            }

        }


        /*
         * ---------------------------------------------------
         * Violations
         * ---------------------------------------------------
         */

        System.out.println();

        System.out.println(
                "Architecture Violations:"
        );


        if (violations.isEmpty()) {

            System.out.println(
                    "  None"
            );

        }

        else {

            for (String violation :
                    violations) {

                System.out.println(
                        "  [VIOLATION] "
                        + violation
                );

            }

        }


        /*
         * ---------------------------------------------------
         * Conclusion
         * ---------------------------------------------------
         */

        System.out.println();

        System.out.println(
        "Conclusion:"
);

if ("LAYERED".equals(
        architectureStyle)) {

    if (violations.isEmpty()) {

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
                + "violations were detected."
        );

    }

}
else if ("MVC".equals(
        architectureStyle)) {

    if (violations.isEmpty()) {

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
                + "violations were detected."
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