package com.softwaredna;

import com.softwaredna.model.Relationship;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.parser.RepositoryParser;

public class Main {

    public static void main(String[] args) {

        try {

            RepositoryParser parser = new RepositoryParser();

            RepositoryModel repository =
                    parser.parseRepository(
                            "sample_projects/inheritance-demo"
                    );

            System.out.println();
            System.out.println("========== RELATIONSHIPS ==========");

            for (Relationship relationship :
                    repository.getRelationships()) {

                System.out.println(
                        relationship.getSource().getName()
                                + " ---- "
                                + relationship.getType()
                                + " ----> "
                                + relationship.getTarget().getName()
                );

            }

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

}