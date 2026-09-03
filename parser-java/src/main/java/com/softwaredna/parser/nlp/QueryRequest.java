package com.softwaredna.parser.nlp;

/**
 * Represents a user's natural-language query after
 * initial NLP interpretation.
 *
 * This object will later be passed through:
 *
 * Question
 *    ↓
 * Intent Detection
 *    ↓
 * Entity Resolution
 *    ↓
 * Query Planning
 */
public class QueryRequest {

    private final String originalQuestion;
    private final QueryIntent intent;
    private final String entityName;

    /**
     * Creates a query request.
     *
     * @param originalQuestion original user question
     * @param intent detected intent
     * @param entityName entity mentioned in the question
     */
    public QueryRequest(
            String originalQuestion,
            QueryIntent intent,
            String entityName) {

        this.originalQuestion = originalQuestion;
        this.intent = intent;
        this.entityName = entityName;
    }

    public String getOriginalQuestion() {
        return originalQuestion;
    }

    public QueryIntent getIntent() {
        return intent;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public String toString() {
        return "QueryRequest{" +
                "originalQuestion='" + originalQuestion + '\'' +
                ", intent=" + intent +
                ", entityName='" + entityName + '\'' +
                '}';
    }
}