/*package com.softwaredna.knowledge;

import com.softwaredna.knowledge.builder.NodeBuilder;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.knowledge.builder.EdgeBuilder;

public class KnowledgeGraphBuilder {

    private final NodeBuilder nodeBuilder;
    private final EdgeBuilder edgeBuilder;

    public KnowledgeGraphBuilder() {

        nodeBuilder = new NodeBuilder();
        edgeBuilder = new EdgeBuilder();

    }

    public KnowledgeGraph build(
            RepositoryModel repository) {

        KnowledgeGraph graph =
                new KnowledgeGraph();

        nodeBuilder.buildNodes(
                repository,
                graph);

        edgeBuilder.buildEdges(
            repository,
            graph);

        return graph;

    }

}*/

package com.softwaredna.knowledge;

import com.softwaredna.knowledge.builder.EdgeBuilder;
import com.softwaredna.knowledge.builder.NodeBuilder;
import com.softwaredna.model.RepositoryModel;

public class KnowledgeGraphBuilder {

    private final NodeBuilder nodeBuilder;
    private final EdgeBuilder edgeBuilder;

    public KnowledgeGraphBuilder() {

        nodeBuilder = new NodeBuilder();
        edgeBuilder = new EdgeBuilder();

    }

    public KnowledgeGraph build(
            RepositoryModel repository) {

        KnowledgeGraph graph =
                new KnowledgeGraph();

        nodeBuilder.buildNodes(
                repository,
                graph);

        edgeBuilder.buildEdges(
                repository,
                graph);

        System.out.println();
        System.out.println("Knowledge Graph Built");
        System.out.println("Nodes : " + graph.getNodes().size());
        System.out.println("Edges : " + graph.getEdges().size());
        System.out.println();

        return graph;

    }

}