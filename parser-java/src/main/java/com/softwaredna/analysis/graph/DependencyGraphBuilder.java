package com.softwaredna.analysis.graph;

import com.softwaredna.model.Relationship;
import com.softwaredna.model.RepositoryModel;

public class DependencyGraphBuilder {

    public DependencyGraph build(
            RepositoryModel repository) {

        DependencyGraph graph =
                new DependencyGraph();

        for (Relationship relationship :
                repository.getRelationships()) {

            graph.addDependency(

                    relationship.getSource().getId(),

                    relationship.getTarget().getId(),

                    relationship.getType()

            );

        }

        return graph;

    }

}