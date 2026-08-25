package com.softwaredna.analysis.architecture;

import com.softwaredna.graph.GraphRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        Set<String> violations =
                new HashSet<>();


        /*
         * ---------------------------------------------------
         * Step 1
         * Classify architectural nodes
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

            if (sourceLayer == null
                    || sourceLayer ==
                    ArchitectureLayer.UNKNOWN) {

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


                if (targetLayer ==
                        ArchitectureLayer.UNKNOWN) {

                    continue;

                }


                /*
                 * ------------------------------------------------
                 * Layered architecture evidence
                 * ------------------------------------------------
                 */

                if (isValidLayerDependency(
                        sourceLayer,
                        targetLayer)) {

                    evidence.add(
                            new ArchitectureEvidence(
                                    displayName(nodeId),
                                    "DEPENDS_ON",
                                    displayName(dependency),
                                    sourceLayer
                                            + " -> "
                                            + targetLayer
                                            + " is a valid layered dependency"
                            )
                    );

                }


                /*
                 * ------------------------------------------------
                 * MVC evidence
                 * ------------------------------------------------
                 */

                if (isValidMVCDependency(
                        sourceLayer,
                        targetLayer)) {

                    evidence.add(
                            new ArchitectureEvidence(
                                    displayName(nodeId),
                                    "DEPENDS_ON",
                                    displayName(dependency),
                                    sourceLayer
                                            + " -> "
                                            + targetLayer
                                            + " supports MVC"
                            )
                    );

                }


                /*
                 * ------------------------------------------------
                 * Architecture violation
                 * ------------------------------------------------
                 */

                if (isArchitectureViolation(
                        sourceLayer,
                        targetLayer)) {

                    violations.add(
                            displayName(nodeId)
                                    + " --DEPENDS_ON--> "
                                    + displayName(dependency)
                                    + " : "
                                    + sourceLayer
                                    + " -> "
                                    + targetLayer
                    );

                }

            }

        }


        /*
         * ---------------------------------------------------
         * Step 3
         * Determine architecture style
         * ---------------------------------------------------
         */

        String architectureStyle =
                determineStyle(
                        layers,
                        evidence
                );


        /*
         * ---------------------------------------------------
         * Step 4
         * Calculate confidence
         * ---------------------------------------------------
         */

        double confidence =
                calculateConfidence(
                        layers,
                        evidence,
                        violations
                );


        /*
         * ---------------------------------------------------
         * Step 5
         * Build report
         * ---------------------------------------------------
         */

        return new ArchitectureReport(
                architectureStyle,
                confidence,
                layers,
                evidence,
                violations
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
         * Remove method / field information.
         */

        int hashIndex =
                value.indexOf('#');


        if (hashIndex >= 0) {

            value =
                    value.substring(
                            0,
                            hashIndex
                    );

        }


        /*
         * ---------------------------------------------------
         * Controller
         * ---------------------------------------------------
         */

        if (value.contains("controller")
                || value.startsWith("api.")
                || value.contains(".api.")
                || value.contains(".controller.")) {

            return ArchitectureLayer.CONTROLLER;

        }


        /*
         * ---------------------------------------------------
         * Service
         * ---------------------------------------------------
         */

        if (value.contains("service")
                || value.startsWith("service.")
                || value.contains(".service.")) {

            return ArchitectureLayer.SERVICE;

        }


        /*
         * ---------------------------------------------------
         * Repository
         * ---------------------------------------------------
         */

        if (value.contains("repository")
                || value.contains("repo")
                || value.startsWith("repository.")
                || value.startsWith("repo.")
                || value.contains(".repository.")
                || value.contains(".repo.")) {

            return ArchitectureLayer.REPOSITORY;

        }


        /*
         * ---------------------------------------------------
         * Model
         * ---------------------------------------------------
         */

        if (value.startsWith("model.")
                || value.contains(".model.")
                || value.startsWith("entity.")
                || value.contains(".entity.")
                || value.startsWith("domain.")
                || value.contains(".domain.")) {

            return ArchitectureLayer.MODEL;

        }


        /*
         * ---------------------------------------------------
         * View
         * ---------------------------------------------------
         */

        if (value.contains("view")
                || value.startsWith("view.")
                || value.contains(".view.")
                || value.contains("template")
                || value.contains(".ui.")) {

            return ArchitectureLayer.VIEW;

        }


        return ArchitectureLayer.UNKNOWN;

    }


    /*
     * =======================================================
     * Layered Architecture Rules
     * =======================================================
     */

    private boolean isValidLayerDependency(
            ArchitectureLayer source,
            ArchitectureLayer target) {


        /*
         * Controller -> Service
         */

        if (source ==
                ArchitectureLayer.CONTROLLER
                && target ==
                ArchitectureLayer.SERVICE) {

            return true;

        }


        /*
         * Controller -> Model
         */

        if (source ==
                ArchitectureLayer.CONTROLLER
                && target ==
                ArchitectureLayer.MODEL) {

            return true;

        }


        /*
         * Service -> Repository
         */

        if (source ==
                ArchitectureLayer.SERVICE
                && target ==
                ArchitectureLayer.REPOSITORY) {

            return true;

        }


        /*
         * Service -> Model
         */

        if (source ==
                ArchitectureLayer.SERVICE
                && target ==
                ArchitectureLayer.MODEL) {

            return true;

        }


        /*
         * Repository -> Model
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
     * MVC Rules
     * =======================================================
     *
     * Controller -> Model
     * Controller -> View
     *
     */

    private boolean isValidMVCDependency(
            ArchitectureLayer source,
            ArchitectureLayer target) {

        if (source ==
                ArchitectureLayer.CONTROLLER
                && target ==
                ArchitectureLayer.MODEL) {

            return true;

        }


        if (source ==
                ArchitectureLayer.CONTROLLER
                && target ==
                ArchitectureLayer.VIEW) {

            return true;

        }


        return false;

    }


    /*
     * =======================================================
     * Architecture Violations
     * =======================================================
     */

    private boolean isArchitectureViolation(
            ArchitectureLayer source,
            ArchitectureLayer target) {


        /*
         * Model should not depend on higher layers.
         */

        if (source ==
                ArchitectureLayer.MODEL) {

            return target !=
                    ArchitectureLayer.MODEL;

        }


        /*
         * View should not depend on Service or Repository.
         */

        if (source ==
                ArchitectureLayer.VIEW) {

            return target ==
                    ArchitectureLayer.SERVICE
                    || target ==
                    ArchitectureLayer.REPOSITORY;

        }


        /*
         * Repository should not depend on
         * Service or Controller.
         */

        if (source ==
                ArchitectureLayer.REPOSITORY) {

            return target ==
                    ArchitectureLayer.SERVICE
                    || target ==
                    ArchitectureLayer.CONTROLLER;

        }


        /*
         * Service should not depend on Controller.
         */

        if (source ==
                ArchitectureLayer.SERVICE) {

            return target ==
                    ArchitectureLayer.CONTROLLER;

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

    boolean viewFound =
            layers.containsValue(
                    ArchitectureLayer.VIEW
            );


    /*
     * ---------------------------------------------------
     * MVC
     * ---------------------------------------------------
     */

    if (controllerFound
            && modelFound
            && viewFound
            && hasMVCEvidence(evidence)) {

        return "MVC";

    }


    /*
     * ---------------------------------------------------
     * Microservices
     * ---------------------------------------------------
     *
     * Multiple independent service boundaries are
     * represented by multiple SERVICE -> REPOSITORY
     * relationships.
     */

    if (isMicroservicesArchitecture(
            layers,
            evidence)) {

        return "MICROSERVICES";

    }


    /*
     * ---------------------------------------------------
     * Layered Architecture
     * ---------------------------------------------------
     */

    if (controllerFound
            && serviceFound
            && repositoryFound
            && modelFound
            && hasLayeredEvidence(evidence)) {

        return "LAYERED";

    }


    /*
     * Partial Layered Architecture
     * ---------------------------------------------------
     */

    if (serviceFound
            && repositoryFound
            && modelFound) {

        return "LAYERED";

    }


    return "UNKNOWN";

}

private boolean isMicroservicesArchitecture(
        Map<String, ArchitectureLayer> layers,
        List<ArchitectureEvidence> evidence) {

    /*
     * A microservices architecture should contain
     * multiple service boundaries.
     */

    int serviceCount = 0;

    for (ArchitectureLayer layer :
            layers.values()) {

        if (layer ==
                ArchitectureLayer.SERVICE) {

            serviceCount++;

        }

    }


    /*
     * Require at least 3 independent services
     * for our current heuristic.
     */

    if (serviceCount < 3) {

        return false;

    }


    /*
     * Count SERVICE -> REPOSITORY relationships.
     */

    int serviceRepositoryEdges = 0;

    Set<String> serviceNames =
            new HashSet<>();


    for (ArchitectureEvidence item :
            evidence) {

        if (item.getExplanation()
                .contains(
                        "SERVICE -> REPOSITORY"
                )) {

            serviceRepositoryEdges++;

            serviceNames.add(
                    item.getSource()
            );

        }

    }


    /*
     * Each service should have its own
     * repository boundary.
     */

    return serviceNames.size() >= 3
            && serviceRepositoryEdges >= 3;

}

    /*
     * =======================================================
     * Evidence Helpers
     * =======================================================
     */

    private boolean hasLayeredEvidence(
            List<ArchitectureEvidence> evidence) {

        for (ArchitectureEvidence item :
                evidence) {

            if (item.getExplanation()
                    .contains(
                            "valid layered dependency"
                    )) {

                return true;

            }

        }

        return false;

    }


    private boolean hasMVCEvidence(
            List<ArchitectureEvidence> evidence) {

        for (ArchitectureEvidence item :
                evidence) {

            if (item.getExplanation()
                    .contains(
                            "supports MVC"
                    )) {

                return true;

            }

        }

        return false;

    }


    /*
     * =======================================================
     * Confidence
     * =======================================================
     */

    private double calculateConfidence(
            Map<String, ArchitectureLayer> layers,
            List<ArchitectureEvidence> evidence,
            Set<String> violations) {


        double score = 0.0;

        /*
 * ---------------------------------------------------
 * Microservices evidence
 * ---------------------------------------------------
 */

boolean microservices =
        isMicroservicesArchitecture(
                layers,
                evidence
        );

if (microservices) {

    score += 0.15;

}


        /*
         * ---------------------------------------------------
         * Layer presence
         * ---------------------------------------------------
         */

        if (layers.containsValue(
                ArchitectureLayer.CONTROLLER)) {

            score += 0.15;

        }


        if (layers.containsValue(
                ArchitectureLayer.SERVICE)) {

            score += 0.15;

        }


        if (layers.containsValue(
                ArchitectureLayer.REPOSITORY)) {

            score += 0.15;

        }


        if (layers.containsValue(
                ArchitectureLayer.MODEL)) {

            score += 0.15;

        }


        /*
         * View contributes to MVC confidence.
         */

        if (layers.containsValue(
                ArchitectureLayer.VIEW)) {

            score += 0.15;

        }


        /*
         * ---------------------------------------------------
         * Evidence
         * ---------------------------------------------------
         */

        if (!evidence.isEmpty()) {

            score += Math.min(
                    evidence.size() * 0.10,
                    0.30
            );

        }


        /*
         * ---------------------------------------------------
         * Violations
         * ---------------------------------------------------
         */

        if (!violations.isEmpty()) {

            score -= Math.min(
                    violations.size() * 0.10,
                    0.30
            );

        }


        /*
         * ---------------------------------------------------
         * Clamp
         * ---------------------------------------------------
         */

        score =
                Math.max(
                        score,
                        0.0
                );

        score =
                Math.min(
                        score,
                        1.0
                );


        return score;

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