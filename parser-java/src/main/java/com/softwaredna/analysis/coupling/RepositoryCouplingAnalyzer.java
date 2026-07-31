package com.softwaredna.analysis.coupling;

import com.softwaredna.analysis.FanInExtractor;
import com.softwaredna.analysis.FanOutExtractor;
import com.softwaredna.analysis.graph.GraphQueryService;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.RepositoryModel;

public class RepositoryCouplingAnalyzer {

    private final FanOutExtractor fanOutExtractor;
    private final FanInExtractor fanInExtractor;
    private final CBOExtractor cboExtractor;

    public RepositoryCouplingAnalyzer() {

        fanOutExtractor = new FanOutExtractor();
        fanInExtractor = new FanInExtractor();
        cboExtractor = new CBOExtractor();

    }

    public void analyze(
            RepositoryModel repository,
            GraphQueryService query) {

        for (ParsedFile file : repository.getFiles()) {

            for (ParsedClass parsedClass : file.getClasses()) {

                parsedClass.getMetrics().setFanOut(
                        fanOutExtractor.extract(
                                query,
                                parsedClass.getId()
                        )
                );

                parsedClass.getMetrics().setFanIn(
                        fanInExtractor.extract(
                                query,
                                parsedClass.getId()
                        )
                );

                parsedClass.getMetrics().setCbo(
                        cboExtractor.extract(
                                query,
                                parsedClass.getId()
                        )
                );

            }

        }

    }

}