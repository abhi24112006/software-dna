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
        prompt.append("5. Give a concise and technically accurate answer.\n\n");

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
}