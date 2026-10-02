package com.softwaredna.analysis.metrics;

import com.softwaredna.model.ClassMetrics;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.RepositoryMetrics;
import com.softwaredna.model.RepositoryModel;

/**
 * Aggregates class-level metrics into repository-level metrics.
 *
 * <p>This class does not recalculate individual metrics.
 * It consumes the metrics already calculated for each ParsedClass
 * and combines them into a RepositoryMetrics object.</p>
 */
public class RepositoryMetricsAggregator {

    /**
     * Aggregates metrics for the supplied repository.
     *
     * @param repository repository whose metrics should be aggregated
     * @return repository-level metrics
     */
    public RepositoryMetrics aggregate(RepositoryModel repository) {

        RepositoryMetrics repositoryMetrics = new RepositoryMetrics();

        if (repository == null) {
            return repositoryMetrics;
        }

        int totalFiles = 0;
        int totalClasses = 0;
        int totalInterfaces = 0;
        int totalEnums = 0;
        int totalRecords = 0;

        int totalMethods = 0;
        int totalFields = 0;
        int totalConstructors = 0;

        int totalLinesOfCode = 0;
        int totalParameters = 0;
        int totalLocalVariables = 0;
        int totalMethodCalls = 0;
        int totalObjectCreations = 0;
        int totalReturnStatements = 0;

        int totalCyclomaticComplexity = 0;
        int maximumCyclomaticComplexity = 0;

        int totalLoops = 0;
        int totalConditionals = 0;
        int maximumNestingDepth = 0;

        int totalFanIn = 0;
        int totalFanOut = 0;
        int totalCbo = 0;

        int maximumDit = 0;
        int totalNoc = 0;
        int totalRfc = 0;

        int classesWithMetrics = 0;
        int methodsWithMetrics = 0;

        if (repository.getFiles() != null) {

            totalFiles = repository.getFiles().size();

            for (ParsedFile file : repository.getFiles()) {

                if (file == null) {
                    continue;
                }

                if (file.getClasses() != null) {

                    totalClasses += file.getClasses().size();

                    for (ParsedClass parsedClass : file.getClasses()) {

                        if (parsedClass == null) {
                            continue;
                        }

                        if (parsedClass.getFields() != null) {
                            totalFields += parsedClass.getFields().size();
                        }

                        if (parsedClass.getConstructors() != null) {
                            totalConstructors +=
                                    parsedClass.getConstructors().size();
                        }

                        if (parsedClass.getMethods() != null) {
                            totalMethods += parsedClass.getMethods().size();
                        }

                        ClassMetrics metrics =
                                parsedClass.getMetrics();

                        if (metrics == null) {
                            continue;
                        }

                        classesWithMetrics++;

                        totalLinesOfCode +=
                                metrics.getTotalLinesOfCode();

                        totalParameters +=
                                metrics.getTotalParameters();

                        totalLocalVariables +=
                                metrics.getTotalLocalVariables();

                        totalMethodCalls +=
                                metrics.getTotalMethodCalls();

                        totalObjectCreations +=
                                metrics.getTotalObjectCreations();

                        totalReturnStatements +=
                                metrics.getTotalReturnStatements();

                        totalCyclomaticComplexity +=
                                metrics.getTotalCyclomaticComplexity();

                        maximumCyclomaticComplexity =
                                Math.max(
                                        maximumCyclomaticComplexity,
                                        metrics.getMaximumCyclomaticComplexity()
                                );

                        totalLoops +=
                                metrics.getTotalLoopCount();

                        totalConditionals +=
                                metrics.getTotalConditionalCount();

                        maximumNestingDepth =
                                Math.max(
                                        maximumNestingDepth,
                                        metrics.getMaximumNestingDepth()
                                );

                        totalFanIn += metrics.getFanIn();
                        totalFanOut += metrics.getFanOut();
                        totalCbo += metrics.getCbo();

                        maximumDit =
                                Math.max(
                                        maximumDit,
                                        metrics.getDit()
                                );

                        totalNoc += metrics.getNoc();
                        totalRfc += metrics.getRfc();

                        if (parsedClass.getMethods() != null) {
                            for (var method : parsedClass.getMethods()) {
                                if (method != null
                                        && method.getMetrics() != null) {
                                    methodsWithMetrics++;
                                }
                            }
                        }
                    }
                }

                if (file.getInterfaces() != null) {
                    totalInterfaces += file.getInterfaces().size();
                }

                if (file.getEnums() != null) {
                    totalEnums += file.getEnums().size();
                }

                if (file.getRecords() != null) {
                    totalRecords += file.getRecords().size();
                }
            }
        }

        repositoryMetrics.setTotalFiles(totalFiles);
        repositoryMetrics.setTotalClasses(totalClasses);
        repositoryMetrics.setTotalInterfaces(totalInterfaces);
        repositoryMetrics.setTotalEnums(totalEnums);
        repositoryMetrics.setTotalRecords(totalRecords);

        repositoryMetrics.setTotalMethods(totalMethods);
        repositoryMetrics.setTotalFields(totalFields);
        repositoryMetrics.setTotalConstructors(totalConstructors);

        repositoryMetrics.setTotalLinesOfCode(totalLinesOfCode);
        repositoryMetrics.setTotalParameters(totalParameters);
        repositoryMetrics.setTotalLocalVariables(totalLocalVariables);
        repositoryMetrics.setTotalMethodCalls(totalMethodCalls);
        repositoryMetrics.setTotalObjectCreations(totalObjectCreations);
        repositoryMetrics.setTotalReturnStatements(totalReturnStatements);

        repositoryMetrics.setTotalCyclomaticComplexity(
                totalCyclomaticComplexity
        );

        repositoryMetrics.setMaximumCyclomaticComplexity(
                maximumCyclomaticComplexity
        );

        repositoryMetrics.setTotalLoops(totalLoops);
        repositoryMetrics.setTotalConditionals(totalConditionals);

        repositoryMetrics.setMaximumNestingDepth(
                maximumNestingDepth
        );

        repositoryMetrics.setMaximumDit(maximumDit);
        repositoryMetrics.setTotalNoc(totalNoc);

        if (classesWithMetrics > 0) {

            repositoryMetrics.setAverageCyclomaticComplexity(
                    (double) totalCyclomaticComplexity
                            / classesWithMetrics
            );

            repositoryMetrics.setAverageFanIn(
                    (double) totalFanIn
                            / classesWithMetrics
            );

            repositoryMetrics.setAverageFanOut(
                    (double) totalFanOut
                            / classesWithMetrics
            );

            repositoryMetrics.setAverageCbo(
                    (double) totalCbo
                            / classesWithMetrics
            );

            repositoryMetrics.setAverageRfc(
                    (double) totalRfc
                            / classesWithMetrics
            );
        }

        if (methodsWithMetrics > 0) {
            repositoryMetrics.setAverageMethodLinesOfCode(
                    (double) totalLinesOfCode
                            / methodsWithMetrics
            );
        }

        return repositoryMetrics;
    }
}