package com.softwaredna.analysis.architecture;

import com.softwaredna.graph.GraphRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchitectureAnalyzer {

    private final GraphRepository graphRepository;


    public ArchitectureAnalyzer(
            GraphRepository graphRepository) {

        this.graphRepository = graphRepository;

    }


    /*
     * =======================================================
     * Architecture Analysis
     * =======================================================
     */

    public ArchitectureReport analyze(
            List<String> nodeIds) {

        Map<String, ArchitectureLayer> layers =
                new HashMap<>();

        List<ArchitectureEvidence> evidence =
                new ArrayList<>();


        /*
         * ---------------------------------------------------
         * Step 1
         * Classify nodes
         * ---------------------------------------------------
         */

        for (String nodeId : nodeIds) {

            ArchitectureLayer layer =
                    classify(nodeId);

            layers.put(
                    nodeId,
                    layer
            );

        }


        /*
         * ---------------------------------------------------
         * Step 2
         * Analyze dependencies
         * ---------------------------------------------------
         */

        for (String nodeId : nodeIds) {

            ArchitectureLayer sourceLayer =
                    layers.get(nodeId);

            if (sourceLayer == null) {
                continue;
            }


            List<String> dependencies =
                    graphRepository.getDependencies(
                            nodeId
                    );


            for (String dependency :
                    dependencies) {

                ArchitectureLayer targetLayer =
                        classify(dependency);


                /*
                 * Only record dependencies that
                 * participate in our architectural
                 * layers.
                 */

                if (targetLayer ==
                        ArchitectureLayer.UNKNOWN) {

                    continue;
                }


                if (isValidLayerDependency(
                        sourceLayer,
                        targetLayer)) {

                    evidence.add(
                            new ArchitectureEvidence(
                                    displayName(nodeId),
                                    "DEPENDS_ON",
                                    displayName(dependency),
                                    sourceLayer
                                            + " → "
                                            + targetLayer
                                            + " is a valid layered dependency"
                            )
                    );

                }

            }

        }


        /*
         * ---------------------------------------------------
         * Step 3
         * Calculate confidence
         * ---------------------------------------------------
         */

        double confidence =
                calculateConfidence(
                        evidence
                );


        /*
         * ---------------------------------------------------
         * Step 4
         * Determine architecture style
         * ---------------------------------------------------
         */

        String architectureStyle =
                determineStyle(
                        layers,
                        evidence
                );


        return new ArchitectureReport(
                architectureStyle,
                confidence,
                layers,
                evidence
        );

    }


    /*
     * =======================================================
     * Layer Classification
     * =======================================================
     */

    private ArchitectureLayer classify(
            String nodeId) {

        if (nodeId == null) {

            return ArchitectureLayer.UNKNOWN;

        }


        String value =
                nodeId.toLowerCase();


        /*
         * Controller
         */

        if (value.contains("controller")
                || value.contains(".api.")
                || value.contains(".controller.")) {

            return ArchitectureLayer.CONTROLLER;

        }


        /*
         * Service
         */

        if (value.contains("service")
                || value.contains(".service.")) {

            return ArchitectureLayer.SERVICE;

        }


        /*
         * Repository
         */

        if (value.contains("repository")
                || value.contains("repo")
                || value.contains(".repository.")
                || value.contains(".repo.")) {

            return ArchitectureLayer.REPOSITORY;

        }


        /*
         * Model
         */

        if (value.contains(".model.")
                || value.contains(".entity.")
                || value.contains(".domain.")) {

            return ArchitectureLayer.MODEL;

        }


        return ArchitectureLayer.UNKNOWN;

    }


    /*
     * =======================================================
     * Layer Dependency Rules
     * =======================================================
     */

    private boolean isValidLayerDependency(
            ArchitectureLayer source,
            ArchitectureLayer target) {


        /*
         * Controller → Service
         */

        if (source ==
                ArchitectureLayer.CONTROLLER
                && target ==
                ArchitectureLayer.SERVICE) {

            return true;

        }


        /*
         * Controller → Model
         *
         * Allowed for the first heuristic version.
         */

        if (source ==
                ArchitectureLayer.CONTROLLER
                && target ==
                ArchitectureLayer.MODEL) {

            return true;

        }


        /*
         * Service → Repository
         */

        if (source ==
                ArchitectureLayer.SERVICE
                && target ==
                ArchitectureLayer.REPOSITORY) {

            return true;

        }


        /*
         * Service → Model
         */

        if (source ==
                ArchitectureLayer.SERVICE
                && target ==
                ArchitectureLayer.MODEL) {

            return true;

        }


        /*
         * Repository → Model
         */

        if (source ==
                ArchitectureLayer.REPOSITORY
                && target ==
                ArchitectureLayer.MODEL) {

            return true;

        }


        return false;

    }


    /*
     * =======================================================
     * Architecture Style
     * =======================================================
     */

    private String determineStyle(
            Map<String, ArchitectureLayer> layers,
            List<ArchitectureEvidence> evidence) {


        boolean controllerFound =
                layers.containsValue(
                        ArchitectureLayer.CONTROLLER
                );

        boolean serviceFound =
                layers.containsValue(
                        ArchitectureLayer.SERVICE
                );

        boolean repositoryFound =
                layers.containsValue(
                        ArchitectureLayer.REPOSITORY
                );

        boolean modelFound =
                layers.containsValue(
                        ArchitectureLayer.MODEL
                );


        /*
         * Strong layered architecture signal.
         */

        if (controllerFound
                && serviceFound
                && repositoryFound
                && modelFound
                && !evidence.isEmpty()) {

            return "LAYERED";

        }


        /*
         * Partial layered architecture.
         */

        if (serviceFound
                && repositoryFound
                && modelFound) {

            return "LAYERED";

        }


        return "UNKNOWN";

    }


    /*
     * =======================================================
     * Confidence
     * =======================================================
     */

    private double calculateConfidence(
            List<ArchitectureEvidence> evidence) {

        if (evidence.isEmpty()) {

            return 0.0;

        }


        /*
         * Initial deterministic heuristic.
         *
         * We cap the confidence at 1.0.
         */

        double confidence =
                0.25
                + (evidence.size() * 0.15);


        return Math.min(
                confidence,
                1.0
        );

    }


    /*
     * =======================================================
     * Display Helpers
     * =======================================================
     */

    private String displayName(
            String nodeId) {

        if (nodeId == null) {

            return "";

        }


        /*
         * Convert method-style graph IDs such as:
         *
         * com.demo.Student#study()
         *
         * to:
         *
         * Student#study()
         *
         *
         * For classes:
         *
         * service.UserService
         *
         * becomes:
         *
         * UserService
         */

        int hashIndex =
                nodeId.lastIndexOf('#');


        if (hashIndex >= 0) {

            String owner =
                    nodeId.substring(
                            0,
                            hashIndex
                    );

            String method =
                    nodeId.substring(
                            hashIndex
                    );

            return simpleName(owner)
                    + method;

        }


        return simpleName(nodeId);

    }


    private String simpleName(
            String value) {

        int dotIndex =
                value.lastIndexOf('.');


        if (dotIndex >= 0
                && dotIndex < value.length() - 1) {

            return value.substring(
                    dotIndex + 1
            );

        }


        return value;

    }

}