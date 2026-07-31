package com.softwaredna.analysis.inheritance;

import com.softwaredna.analysis.graph.GraphQueryService;
import com.softwaredna.analysis.graph.TypedDependency;

public class DITMetric {

    public int compute(
            GraphQueryService query,
            String classId) {

        int depth = 0;
        String current = classId;

        while (true) {

            var parents =
                    query.getParents(current);

            if (parents.isEmpty()) {
                break;
            }

            TypedDependency parent =
                    parents.iterator().next();

            current = parent.getTargetId();

            depth++;

        }

        return depth;

    }

}