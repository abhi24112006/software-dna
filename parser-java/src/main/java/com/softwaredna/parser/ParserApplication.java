package com.softwaredna.parser;
import com.softwaredna.util.IdentifierFormatter;
import com.softwaredna.model.*;
import com.softwaredna.printer.RepositoryPrinter;

public class ParserApplication {

    public static void main(String[] args) {

        try {

            RepositoryParser parser = new RepositoryParser();

            RepositoryModel repository =
                    parser.parseRepository("../sample_projects");

            RepositoryPrinter repositoryPrinter =
                new RepositoryPrinter();

        repositoryPrinter.print(repository);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}