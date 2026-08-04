package com.softwaredna;

import com.softwaredna.analysis.repository.RepositoryAnalyzer;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.KnowledgeGraphBuilder;
import com.softwaredna.knowledge.printer.KnowledgeGraphPrinter;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.parser.RepositoryParser;
import com.softwaredna.printer.RepositoryPrinter;

public class Main {

    public static void main(String[] args) {

        try {

            RepositoryParser parser =
                    new RepositoryParser();

            RepositoryModel repository =
                    parser.parseRepository(
                            "sample_projects/inheritance-demo"
                    );

            RepositoryAnalyzer analyzer =
                    new RepositoryAnalyzer();

            analyzer.analyze(repository);

            System.out.println();
            System.out.println("========== BEFORE GRAPH ==========");

            KnowledgeGraphBuilder graphBuilder =
                    new KnowledgeGraphBuilder();

            KnowledgeGraph graph =
                    graphBuilder.build(repository);

            System.out.println("========== AFTER BUILD ==========");

            KnowledgeGraphPrinter graphPrinter =
                    new KnowledgeGraphPrinter();

            System.out.println("========== BEFORE PRINT ==========");

            graphPrinter.print(graph);

            System.out.println("========== AFTER PRINT ==========");

            RepositoryPrinter printer =
                    new RepositoryPrinter();

            printer.print(repository);

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

}