package com.softwaredna.analysis.coupling;

import com.softwaredna.analysis.graph.GraphQueryService;

public class CBOExtractor {

    private final CouplingAnalyzer couplingAnalyzer;

    public CBOExtractor() {

        couplingAnalyzer = new CouplingAnalyzer();

    }

    public int extract(
            GraphQueryService query,
            String classId) {

        return couplingAnalyzer
                .analyze(query, classId)
                .getCbo();

    }

}