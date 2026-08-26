package com.softwaredna.analysis.architecture;

public class ArchitectureDiffPrinter {

    public void print(
            ArchitectureDiff diff) {

        System.out.println();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Architecture Evolution"
        );

        System.out.println(
                "======================================"
        );


        /*
         * ===================================================
         * Architecture Style
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "Architecture Change:"
        );

        System.out.println(
                "  "
                        + diff.getPreviousStyle()
                        + " -> "
                        + diff.getCurrentStyle()
        );


        /*
         * ===================================================
         * Health Change
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "Architecture Health:"
        );

        System.out.printf(
                "  Previous : %.0f / 100%n",
                diff.getPreviousHealth()
        );

        System.out.printf(
                "  Current  : %.0f / 100%n",
                diff.getCurrentHealth()
        );

        System.out.printf(
                "  Change   : %+.0f%n",
                diff.getHealthChange()
        );


        /*
         * ===================================================
         * Added Dependencies
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "Added Dependencies:"
        );


        if (diff.getAddedDependencies()
                .isEmpty()) {

            System.out.println(
                    "  None"
            );

        }
        else {

            for (String dependency :
                    diff.getAddedDependencies()) {

                System.out.println(
                        "  + "
                                + dependency
                );

            }

        }


        /*
         * ===================================================
         * Removed Dependencies
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "Removed Dependencies:"
        );


        if (diff.getRemovedDependencies()
                .isEmpty()) {

            System.out.println(
                    "  None"
            );

        }
        else {

            for (String dependency :
                    diff.getRemovedDependencies()) {

                System.out.println(
                        "  - "
                                + dependency
                );

            }

        }


        /*
         * ===================================================
         * New Anomalies
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "New Architecture Anomalies:"
        );


        if (diff.getNewAnomalies()
                .isEmpty()) {

            System.out.println(
                    "  None"
            );

        }
        else {

            for (String anomaly :
                    diff.getNewAnomalies()) {

                System.out.println(
                        "  [NEW] "
                                + anomaly
                );

            }

        }


        /*
         * ===================================================
         * Resolved Anomalies
         * ===================================================
         */

        System.out.println();

        System.out.println(
                "Resolved Architecture Anomalies:"
        );


        if (diff.getResolvedAnomalies()
                .isEmpty()) {

            System.out.println(
                    "  None"
            );

        }
        else {

            for (String anomaly :
                    diff.getResolvedAnomalies()) {

                System.out.println(
                        "  [RESOLVED] "
                                + anomaly
                );

            }

        }


        System.out.println();

    }

}