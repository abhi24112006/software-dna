package com.softwaredna.analysis;

import com.softwaredna.analysis.graph.GraphQueryService;

public class FanOutExtractor {

    public int extract(
            GraphQueryService query,
            String classId) {

        return query
                .getOutgoingDependencies(classId)
                .size();

    }

}