package com.softwaredna.analysis.architecture;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ArchitectureReport {

    private final String architectureStyle;

    private final double confidence;

    private final Map<String, ArchitectureLayer> layers;

    private final List<ArchitectureEvidence> evidence;


    public ArchitectureReport(
            String architectureStyle,
            double confidence,
            Map<String, ArchitectureLayer> layers,
            List<ArchitectureEvidence> evidence) {

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


        System.out.println();

        System.out.println(
                "Architecture Style : "
                        + architectureStyle
        );

        System.out.printf(
                "Confidence          : %.2f%n",
                confidence
        );


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


        System.out.println();

        System.out.println(
                "Evidence:"
        );


        for (ArchitectureEvidence item :
                evidence) {

            System.out.println(
                    "  -> "
                    + item
            );

        }


        System.out.println();

    }

}