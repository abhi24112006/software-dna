package com.softwaredna.analysis.repository;

import com.softwaredna.analysis.coupling.RepositoryCouplingAnalyzer;
import com.softwaredna.analysis.graph.DependencyGraph;
import com.softwaredna.analysis.graph.DependencyGraphBuilder;
import com.softwaredna.analysis.graph.GraphQueryService;
import com.softwaredna.analysis.inheritance.RepositoryInheritanceAnalyzer;
import com.softwaredna.analysis.metrics.ClassMetricAggregator;
import com.softwaredna.analysis.rfc.RepositoryRFCAnalyzer;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.RepositoryModel;

public class RepositoryAnalyzer {

    private final ClassMetricAggregator classMetricAggregator;
    private final DependencyGraphBuilder graphBuilder;
    private final RepositoryCouplingAnalyzer couplingAnalyzer;
    private final RepositoryInheritanceAnalyzer inheritanceAnalyzer;
    private final RepositoryRFCAnalyzer rfcAnalyzer;

    public RepositoryAnalyzer() {

        classMetricAggregator =
                new ClassMetricAggregator();

        graphBuilder =
                new DependencyGraphBuilder();

        couplingAnalyzer =
                new RepositoryCouplingAnalyzer();

        inheritanceAnalyzer =
                new RepositoryInheritanceAnalyzer();

        rfcAnalyzer =
                new RepositoryRFCAnalyzer();
    }

    public void analyze(
            RepositoryModel repository) {

        /*
         * Phase 1
         * Common class-level metric aggregation.
         *
         * Language-specific parsers provide normalized
         * ParsedClass and MethodMetrics objects.
         *
         * The common Metric Engine aggregates those
         * method-level metrics into ClassMetrics.
         *
         * Classes that already have metrics are skipped
         * to preserve the existing Java parsing behavior.
         */
        aggregateClassMetrics(repository);

        /*
         * Phase 2
         * Build dependency graph.
         */
        DependencyGraph graph =
                graphBuilder.build(repository);

        GraphQueryService query =
                new GraphQueryService(graph);

        /*
         * Phase 3
         * Coupling analysis.
         */
        couplingAnalyzer.analyze(
                repository,
                query
        );

        /*
         * Phase 4
         * Inheritance analysis.
         */
        inheritanceAnalyzer.analyze(
                repository,
                query
        );

        /*
         * Phase 5
         * Response For a Class analysis.
         */
        rfcAnalyzer.analyze(
                repository,
                query
        );

        /*
         * Future phases:
         *
         * architectureAnalyzer.analyze(...)
         * packageAnalyzer.analyze(...)
         * technicalDebtAnalyzer.analyze(...)
         */
    }

    /**
     * Applies the common class-level metric aggregation
     * engine to every parsed class in the repository.
     *
     * <p>The aggregation is language-independent because
     * ClassMetricAggregator operates on the normalized
     * ParsedClass model rather than a language-specific AST.</p>
     *
     * @param repository parsed repository model
     */
    private void aggregateClassMetrics(
            RepositoryModel repository) {

        if (repository == null) {
            throw new IllegalArgumentException(
                    "RepositoryModel cannot be null."
            );
        }

        for (ParsedFile file :
                repository.getFiles()) {

            if (file == null) {
                continue;
            }

            for (ParsedClass parsedClass :
                    file.getClasses()) {

                if (parsedClass == null) {
                    continue;
                }

                /*
                 * Java classes may already have metrics
                 * calculated by ParsedFileBuilder.
                 *
                 * Python and JavaScript classes normally
                 * arrive here without ClassMetrics.
                 *
                 * Only calculate missing metrics so that
                 * existing Java behavior is preserved.
                 */
                if (parsedClass.getMetrics() == null) {

                    parsedClass.setMetrics(
                            classMetricAggregator.aggregate(
                                    parsedClass
                            )
                    );
                }
            }
        }
    }
}