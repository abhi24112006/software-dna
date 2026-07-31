package com.softwaredna.analysis.inheritance;

import com.softwaredna.analysis.graph.DependencyGraph;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.analysis.graph.GraphQueryService;

public class RepositoryInheritanceAnalyzer {

    private final DITMetric ditMetric;
    private final NOCMetric nocMetric;

    public RepositoryInheritanceAnalyzer() {

        ditMetric = new DITMetric();
        nocMetric = new NOCMetric();

    }

    public void analyze(
            RepositoryModel repository,
            GraphQueryService query) {

        for (ParsedFile file : repository.getFiles()) {

            for (ParsedClass parsedClass : file.getClasses()) {

                parsedClass.getMetrics().setDit(
                        ditMetric.compute(
                                query,
                                parsedClass.getId()
                        )
                );

                parsedClass.getMetrics().setNoc(
                        nocMetric.compute(
                                query,
                                parsedClass.getId()
                        )
                );

            }

        }

    }

}