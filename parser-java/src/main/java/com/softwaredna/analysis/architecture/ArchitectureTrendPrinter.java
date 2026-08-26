package com.softwaredna.analysis.architecture;

public class ArchitectureTrendPrinter {

    public void print(
            ArchitectureTrend trend) {

        System.out.println();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Architecture Trend"
        );

        System.out.println(
                "======================================"
        );


        System.out.println();

        System.out.println(
                "Health Trend:"
        );

        System.out.printf(
                "  Starting Health : %.0f / 100%n",
                trend.getStartingHealth()
        );

        System.out.printf(
                "  Current Health  : %.0f / 100%n",
                trend.getCurrentHealth()
        );

        System.out.printf(
                "  Health Change   : %+.0f%n",
                trend.getHealthChange()
        );


        System.out.println();

        System.out.println(
                "Architecture Changes:"
        );

        System.out.println(
                "  Total Changes : "
                        + trend.getTotalChanges()
        );


        System.out.println();

        System.out.println(
                "Dependency Trend:"
        );

        System.out.println(
                "  Added Dependencies   : "
                        + trend.getTotalAddedDependencies()
        );

        System.out.println(
                "  Removed Dependencies : "
                        + trend.getTotalRemovedDependencies()
        );


        System.out.println();

        System.out.println(
                "Anomaly Trend:"
        );

        System.out.println(
                "  New Anomalies      : "
                        + trend.getTotalNewAnomalies()
        );

        System.out.println(
                "  Resolved Anomalies : "
                        + trend.getTotalResolvedAnomalies()
        );


        System.out.println();

        /*
         * ===================================================
         * Overall Trend
         * ===================================================
         */

        System.out.println(
                "Overall Trend:"
        );


        double healthChange =
                trend.getHealthChange();


        if (healthChange > 0) {

            System.out.println(
                    "  Architecture health "
                            + "is improving."
            );

        }
        else if (healthChange < 0) {

            System.out.println(
                    "  Architecture health "
                            + "is declining."
            );

        }
        else {

            System.out.println(
                    "  Architecture health "
                            + "is stable."
            );

        }


        if (trend.getTotalNewAnomalies()
                > trend.getTotalResolvedAnomalies()) {

            System.out.println(
                    "  More architectural anomalies "
                            + "were introduced than resolved."
            );

        }
        else if (
                trend.getTotalResolvedAnomalies()
                        > trend.getTotalNewAnomalies()) {

            System.out.println(
                    "  More architectural anomalies "
                            + "were resolved than introduced."
            );

        }
        else {

            System.out.println(
                    "  Architectural anomaly count "
                            + "is balanced."
            );

        }


        System.out.println();

    }

}