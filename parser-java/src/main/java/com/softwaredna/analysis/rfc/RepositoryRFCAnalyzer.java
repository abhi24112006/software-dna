package com.softwaredna.analysis.rfc;

import com.softwaredna.analysis.graph.GraphQueryService;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.RepositoryModel;

public class RepositoryRFCAnalyzer {

    private final RFCAnalyzer rfcAnalyzer;

    public RepositoryRFCAnalyzer() {

        rfcAnalyzer = new RFCAnalyzer();

    }

    public void analyze(
            RepositoryModel repository,
            GraphQueryService query) {

        for (ParsedFile file : repository.getFiles()) {

            for (ParsedClass parsedClass : file.getClasses()) {

                RFCMetrics metrics =
                        rfcAnalyzer.analyze(
                                query,
                                parsedClass
                        );

                parsedClass.getMetrics().setRfc(
                        metrics.getRfc()
                );

            }

        }

    }

}