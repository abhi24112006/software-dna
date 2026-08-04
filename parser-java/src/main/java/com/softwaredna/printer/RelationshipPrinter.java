package com.softwaredna.printer;

import com.softwaredna.model.Relationship;
import com.softwaredna.model.RelationshipType;
import com.softwaredna.util.IdentifierFormatter;

import java.util.Set;

public class RelationshipPrinter {

    public void print(Set<Relationship> relationships) {

        System.out.println();

        System.out.println("======================================");
        System.out.println("Relationships");
        System.out.println("======================================");

        System.out.println(
                "Total Relationships : "
                        + relationships.size());

        if (relationships.isEmpty()) {

            System.out.println("None");
            return;

        }

        for (Relationship relationship : relationships) {

            String source =
                    IdentifierFormatter.format(
                            relationship.getSource());

            String target =
                    IdentifierFormatter.format(
                            relationship.getTarget());

            if (relationship.getType()
                    == RelationshipType.METHOD_CALL_INTERNAL) {

                System.out.println(
                        source
                                + " ---- METHOD_CALL_INTERNAL ----> "
                                + target);

            }
            else if (relationship.getType()
                    == RelationshipType.METHOD_CALL_EXTERNAL) {

                System.out.println(
                        source
                                + " ---- METHOD_CALL_EXTERNAL ----> "
                                + target);

            }
            else {

                System.out.println(
                        source
                                + " ---- "
                                + relationship.getType()
                                + " ----> "
                                + target);

            }

        }

    }

}