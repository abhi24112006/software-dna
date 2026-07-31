package com.softwaredna.analysis.coupling;

import com.softwaredna.analysis.graph.GraphQueryService;
import com.softwaredna.analysis.graph.TypedDependency;
import com.softwaredna.model.RelationshipType;

public class CouplingAnalyzer {

    public CouplingMetrics analyze(
            GraphQueryService query,
            String classId) {

        CouplingMetrics metrics =
                new CouplingMetrics();

        /*
         * CBO
         *
         * Number of distinct coupled classes.
         */
        metrics.setCbo(
                query.getOutgoingDependencies(classId).size()
        );

        /*
         * Count each relationship type.
         */
        for (TypedDependency dependency :
                query.getOutgoingDependencies(classId)) {

            for (RelationshipType relationshipType :
                    dependency.getRelationshipTypes()) {

                metrics.incrementRelationshipCount(
                        relationshipType
                );

            }

        }

        return metrics;

    }

}