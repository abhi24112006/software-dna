package com.softwaredna.analysis;

import com.softwaredna.analysis.graph.GraphQueryService;

public class FanInExtractor {

    public int extract(
            GraphQueryService query,
            String classId) {

        return query
                .getIncomingDependencies(classId)
                .size();

    }

}