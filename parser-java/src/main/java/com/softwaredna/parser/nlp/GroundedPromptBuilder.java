package com.softwaredna.parser.nlp;

import com.softwaredna.knowledge.GraphNode;

/**
 * Builds prompts for an LLM using only graph-derived information.
 *
 * The prompt explicitly instructs the LLM to treat the supplied
 * graph facts as the only source of truth.
 */
public class GroundedPromptBuilder {

    /**
     * Builds a grounded prompt from the supplied context.
     *
     * @param context graph-derived context
     * @return prompt suitable for an LLM
     */
    public String build(GroundedContext context) {

        if (context == null) {
            throw new IllegalArgumentException(
                    "GroundedContext cannot be null."
            );
        }

        StringBuilder prompt = new StringBuilder();

        prompt.append("You are answering a software architecture question ")
                .append("using facts retrieved from a software knowledge graph.\n\n");

        prompt.append("IMPORTANT RULES:\n");
        prompt.append("1. Use only the graph-derived facts provided below.\n");
        prompt.append("2. Do not invent classes, methods, dependencies, callers, ")
                .append("callees, or other relationships.\n");
        prompt.append("3. Do not assume relationships that are not explicitly ")
                .append("present in the graph facts.\n");
        prompt.append("4. If the supplied facts are insufficient to answer ")
                .append("the question, say so clearly.\n");
        prompt.append("5. Give a concise and technically accurate answer.\n");
        prompt.append("6. The QUERY INTENT defines how the target entity and ")
                .append("graph-derived entities are related.\n\n");

        prompt.append("USER QUESTION:\n");
        prompt.append(context.getQuestion())
                .append("\n\n");

        prompt.append("QUERY INTENT:\n");
        prompt.append(context.getIntent())
                .append("\n\n");

        prompt.append("TARGET ENTITY:\n");
        GraphNode entity = context.getEntity();

        prompt.append(entity.getName())
                .append(" [")
                .append(entity.getType())
                .append("]\n\n");

        prompt.append("RELATIONSHIP INTERPRETATION:\n");

        appendRelationshipInterpretation(
                prompt,
                context
        );

        prompt.append("\n");

        prompt.append("GRAPH-DERIVED FACTS:\n");

        if (context.getNodes().isEmpty()) {

            prompt.append("- No related graph entities were found.\n");

        } else {

            for (GraphNode node : context.getNodes()) {

                prompt.append("- ")
                        .append(node.getName())
                        .append(" [")
                        .append(node.getType())
                        .append("]\n");
            }
        }

        prompt.append("\n");
        prompt.append("ANSWER:\n");

        return prompt.toString();
    }

    /**
     * Explains how the query result nodes relate to the target entity
     * according to the detected query intent.
     */
    private void appendRelationshipInterpretation(
            StringBuilder prompt,
            GroundedContext context) {

        QueryIntent intent = context.getIntent();

        switch (intent) {

            case DEPENDENCIES:
                prompt.append("- The target entity DEPENDS ON each entity ")
                        .append("listed in GRAPH-DERIVED FACTS.\n");
                break;

            case DEPENDENTS:
                prompt.append("- Each entity listed in GRAPH-DERIVED FACTS ")
                        .append("DEPENDS ON the target entity.\n");
                break;

            case CALLEES:
                prompt.append("- The target entity CALLS each method ")
                        .append("listed in GRAPH-DERIVED FACTS.\n");
                break;

            case CALLERS:
                prompt.append("- Each method listed in GRAPH-DERIVED FACTS ")
                        .append("CALLS the target method.\n");
                break;

            case SUBCLASSES:
                prompt.append("- Each class listed in GRAPH-DERIVED FACTS ")
                        .append("EXTENDS the target class.\n");
                break;

            case SUPERCLASS:
                prompt.append("- The target class EXTENDS the class listed ")
                        .append("in GRAPH-DERIVED FACTS.\n");
                break;

            case IMPLEMENTED_INTERFACES:
                prompt.append("- The target class IMPLEMENTS each interface ")
                        .append("listed in GRAPH-DERIVED FACTS.\n");
                break;

            case IMPLEMENTATIONS:
                prompt.append("- Each class listed in GRAPH-DERIVED FACTS ")
                        .append("IMPLEMENTS the target interface.\n");
                break;

            case IMPACT:
    prompt.append("- GRAPH-DERIVED FACTS contains the entities ")
            .append("identified by the impact analysis as potentially ")
            .append("affected by changes to the TARGET ENTITY.\n");

    prompt.append("- Every entity listed in GRAPH-DERIVED FACTS MUST ")
            .append("be reported as an impacted entity.\n");

    prompt.append("- Do not exclude any entity listed in ")
            .append("GRAPH-DERIVED FACTS.\n");

    prompt.append("- Do not state that any listed entity is not impacted.\n");

    prompt.append("- Do not independently determine or reinterpret ")
            .append("whether an entity is impacted.\n");

    prompt.append("- The impact analysis has already been performed ")
            .append("by the graph engine.\n");

    prompt.append("- Do not infer additional affected entities or ")
            .append("consequences beyond the supplied facts.\n");

    prompt.append("- Distinguish classes and methods using their ")
            .append("provided entity types.\n");
    break;
        }
    }
}