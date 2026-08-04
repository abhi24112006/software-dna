package com.softwaredna.analysis.repository;

import com.softwaredna.analysis.coupling.RepositoryCouplingAnalyzer;
import com.softwaredna.analysis.graph.DependencyGraph;
import com.softwaredna.analysis.graph.DependencyGraphBuilder;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.analysis.inheritance.RepositoryInheritanceAnalyzer;
import com.softwaredna.analysis.graph.GraphQueryService;
import com.softwaredna.analysis.rfc.RepositoryRFCAnalyzer;

public class RepositoryAnalyzer {

    private final DependencyGraphBuilder graphBuilder;
    private final RepositoryCouplingAnalyzer couplingAnalyzer;
    private final RepositoryInheritanceAnalyzer inheritanceAnalyzer;
    private final RepositoryRFCAnalyzer rfcAnalyzer;

    public RepositoryAnalyzer() {

        graphBuilder = new DependencyGraphBuilder();
        couplingAnalyzer = new RepositoryCouplingAnalyzer();
        inheritanceAnalyzer = new RepositoryInheritanceAnalyzer();
        rfcAnalyzer = new RepositoryRFCAnalyzer();

    }

    public void analyze(
            RepositoryModel repository) {

        /*
         * Phase 1
         * Build dependency graph.
         */
        DependencyGraph graph =
                graphBuilder.build(repository);

        GraphQueryService query =
                new GraphQueryService(graph);

        /*
         * Phase 2
         * Coupling analysis.
         */
        couplingAnalyzer.analyze(repository, query);

        inheritanceAnalyzer.analyze(repository, query);

        rfcAnalyzer.analyze(repository, query);

        /*
         * Future phases:
         *
         * inheritanceAnalyzer.analyze(...)
         * architectureAnalyzer.analyze(...)
         * packageAnalyzer.analyze(...)
         * technicalDebtAnalyzer.analyze(...)
         */

    }

}