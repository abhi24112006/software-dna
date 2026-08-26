package com.softwaredna.analysis.architecture;

import java.util.ArrayList;
import java.util.List;

public class ArchitectureRecommendationAnalyzer {


    public List<ArchitectureRecommendation>
    analyze(ArchitectureReport report) {

        List<ArchitectureRecommendation>
                recommendations =
                new ArrayList<>();


        /*
         * ===================================================
         * Analyze architectural anomalies
         * ===================================================
         */

        for (ArchitectureAnomaly anomaly :
                report.getAnomalies()) {

            ArchitectureLayer source =
                    anomaly.getSourceLayer();

            ArchitectureLayer target =
                    anomaly.getTargetLayer();


            /*
             * ------------------------------------------------
             * Repository -> Service
             * ------------------------------------------------
             */

            if (source ==
                    ArchitectureLayer.REPOSITORY
                    && target ==
                    ArchitectureLayer.SERVICE) {

                recommendations.add(
                        new ArchitectureRecommendation(

                                "Dependency Inversion",

                                "HIGH",

                                anomaly.getSource()
                                        + " depends on "
                                        + anomaly.getTarget()
                                        + ".",

                                "The Repository layer is "
                                        + "coupled to business "
                                        + "logic in the Service "
                                        + "layer.",

                                "Remove the Service dependency "
                                        + "from the Repository. "
                                        + "If communication is "
                                        + "required, introduce "
                                        + "an appropriate abstraction.",

                                "Better layer isolation, "
                                        + "lower coupling, and "
                                        + "improved maintainability."
                        )
                );

            }


            /*
             * ------------------------------------------------
             * Repository -> Controller
             * ------------------------------------------------
             */

            else if (source ==
                    ArchitectureLayer.REPOSITORY
                    && target ==
                    ArchitectureLayer.CONTROLLER) {

                recommendations.add(
                        new ArchitectureRecommendation(

                                "Repository Layer Isolation",

                                "HIGH",

                                anomaly.getSource()
                                        + " depends on "
                                        + anomaly.getTarget()
                                        + ".",

                                "Persistence code depends "
                                        + "on the presentation "
                                        + "layer.",

                                "Remove the Controller "
                                        + "dependency from the "
                                        + "Repository.",

                                "Improved separation between "
                                        + "persistence and "
                                        + "presentation concerns."
                        )
                );

            }


            /*
             * ------------------------------------------------
             * Service -> Controller
             * ------------------------------------------------
             */

            else if (source ==
                    ArchitectureLayer.SERVICE
                    && target ==
                    ArchitectureLayer.CONTROLLER) {

                recommendations.add(
                        new ArchitectureRecommendation(

                                "Service Layer Isolation",

                                "HIGH",

                                anomaly.getSource()
                                        + " depends on "
                                        + anomaly.getTarget()
                                        + ".",

                                "Business logic depends "
                                        + "on presentation "
                                        + "logic.",

                                "Remove the Controller "
                                        + "dependency from "
                                        + "the Service.",

                                "Better separation of "
                                        + "business and "
                                        + "presentation logic."
                        )
                );

            }


            /*
             * ------------------------------------------------
             * View -> Service
             * ------------------------------------------------
             */

            else if (source ==
                    ArchitectureLayer.VIEW
                    && target ==
                    ArchitectureLayer.SERVICE) {

                recommendations.add(
                        new ArchitectureRecommendation(

                                "View Layer Isolation",

                                "HIGH",

                                anomaly.getSource()
                                        + " depends on "
                                        + anomaly.getTarget()
                                        + ".",

                                "The View directly "
                                        + "depends on business "
                                        + "logic.",

                                "Move business logic "
                                        + "out of the View "
                                        + "and access it "
                                        + "through the "
                                        + "appropriate application "
                                        + "boundary.",

                                "Cleaner presentation logic "
                                        + "and improved "
                                        + "testability."
                        )
                );

            }


            /*
             * ------------------------------------------------
             * View -> Repository
             * ------------------------------------------------
             */

            else if (source ==
                    ArchitectureLayer.VIEW
                    && target ==
                    ArchitectureLayer.REPOSITORY) {

                recommendations.add(
                        new ArchitectureRecommendation(

                                "Persistence Boundary",

                                "HIGH",

                                anomaly.getSource()
                                        + " depends on "
                                        + anomaly.getTarget()
                                        + ".",

                                "The View directly "
                                        + "accesses persistence "
                                        + "logic.",

                                "Remove direct Repository "
                                        + "access from the View "
                                        + "and route persistence "
                                        + "operations through "
                                        + "the appropriate "
                                        + "application layer.",

                                "Reduced coupling and "
                                        + "better separation "
                                        + "of presentation "
                                        + "and persistence."
                        )
                );

            }


            /*
             * ------------------------------------------------
             * Model -> Higher Layer
             * ------------------------------------------------
             */

            else if (source ==
                    ArchitectureLayer.MODEL) {

                recommendations.add(
                        new ArchitectureRecommendation(

                                "Model Isolation",

                                "HIGH",

                                anomaly.getSource()
                                        + " depends on "
                                        + anomaly.getTarget()
                                        + ".",

                                "The Model depends on "
                                        + "another architectural "
                                        + "layer.",

                                "Remove the dependency "
                                        + "from the Model "
                                        + "toward the higher "
                                        + "architectural layer.",

                                "A more independent domain "
                                        + "model and reduced "
                                        + "architectural coupling."
                        )
                );

            }

        }


        /*
         * ===================================================
         * Clean architecture
         * ===================================================
         */

        if (recommendations.isEmpty()) {

            recommendations.add(
                    new ArchitectureRecommendation(

                            "Architecture Maintenance",

                            "INFO",

                            "No architectural anomalies "
                                    + "were detected.",

                            "The repository currently "
                                    + "follows its recognized "
                                    + "architectural boundaries.",

                            "Continue monitoring "
                                    + "architectural dependencies "
                                    + "as the codebase evolves.",

                            "Helps preserve architectural "
                                    + "quality over time."
                    )
            );

        }


        return recommendations;

    }

}