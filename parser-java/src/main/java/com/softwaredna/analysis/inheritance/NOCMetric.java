package com.softwaredna.analysis.inheritance;

import com.softwaredna.analysis.graph.GraphQueryService;

public class NOCMetric {

    public int compute(
            GraphQueryService query,
            String classId) {

        return query
                .getChildren(classId)
                .size();

    }

}