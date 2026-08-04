/*package com.softwaredna.knowledge.printer;

import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;

public class KnowledgeGraphPrinter {

    public void print(
            KnowledgeGraph graph) {

        System.out.println();
        System.out.println("======================================");
        System.out.println("Knowledge Graph");
        System.out.println("======================================");

        System.out.println();
        System.out.println("Nodes");
        System.out.println("--------------------------------------");

        for (GraphNode node : graph.getNodes()) {

            System.out.println(
                    node.getType()
                            + " : "
                            + node.getName());

        }

        System.out.println();

        System.out.println("Edges");
        System.out.println("--------------------------------------");

        for (GraphEdge edge : graph.getEdges()) {

            System.out.println(

                    edge.getSource().getName()

                            + " --"

                            + edge.getType()

                            + "--> "

                            + edge.getTarget().getName()

            );

        }

    }

}*/

package com.softwaredna.knowledge.printer;

import com.softwaredna.knowledge.GraphEdge;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;

public class KnowledgeGraphPrinter {

    public void print(
            KnowledgeGraph graph) {

        System.out.println();
        System.out.println("======================================");
        System.out.println("Knowledge Graph");
        System.out.println("======================================");

        System.out.println();
        System.out.println("Total Nodes : " + graph.getNodes().size());
        System.out.println("Total Edges : " + graph.getEdges().size());

        System.out.println();
        System.out.println("Nodes");
        System.out.println("--------------------------------------");

        for (GraphNode node : graph.getNodes()) {

            System.out.println(
                    node.getType()
                            + " : "
                            + node.getName());

        }

        System.out.println();
        System.out.println("Edges");
        System.out.println("--------------------------------------");

        for (GraphEdge edge : graph.getEdges()) {

            System.out.println(
                    edge.getSource().getName()
                            + " -- "
                            + edge.getType()
                            + " --> "
                            + edge.getTarget().getName());

        }

        System.out.println();
        System.out.println("======================================");
        System.out.println();

    }

}