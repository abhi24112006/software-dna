package com.softwaredna;
import com.softwaredna.scope.Scope;
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

        Scope scope = new Scope();

scope.declareVariable("teacher", "Teacher");
scope.declareVariable("student", "Student");

System.out.println(scope.resolveVariable("teacher"));
System.out.println(scope.resolveVariable("student"));
System.out.println(scope.resolveVariable("course"));

scope.clear();

System.out.println(scope.resolveVariable("teacher"));

    }

}