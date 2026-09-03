package com.softwaredna.parser.nlp;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.query.KnowledgeGraphQuery;

/**
 * End-to-end natural-language query engine.
 *
 * Pipeline:
 *
 * Natural-language question
 *        ↓
 * QueryIntentDetector
 *        ↓
 * Entity extraction
 *        ↓
 * QueryRequest
 *        ↓
 * EntityResolver
 *        ↓
 * QueryPlanner
 *        ↓
 * QueryExecutor
 *        ↓
 * QueryResult
 *
 * This is the deterministic NLP foundation.
 * An LLM can be added later for more flexible language
 * understanding and answer generation.
 */
public class NaturalLanguageQueryEngine {

    private final QueryIntentDetector intentDetector;
    private final EntityResolver entityResolver;
    private final QueryPlanner queryPlanner;
    private final QueryExecutor queryExecutor;

    public NaturalLanguageQueryEngine(KnowledgeGraph graph) {

        if (graph == null) {
            throw new IllegalArgumentException(
                    "KnowledgeGraph cannot be null."
            );
        }

        KnowledgeGraphQuery graphQuery =
                new KnowledgeGraphQuery(graph);

        this.intentDetector = new QueryIntentDetector();
        this.entityResolver = new EntityResolver(graph);
        this.queryPlanner = new QueryPlanner();
        this.queryExecutor = new QueryExecutor(graphQuery);
    }

    /**
     * Processes a natural-language question.
     *
     * @param question natural-language question
     * @return structured graph query result
     */
    public QueryResult ask(String question) {

        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException(
                    "Question cannot be null or blank."
            );
        }

        /*
         * Step 1: Detect intent.
         */
        QueryIntent intent =
                intentDetector.detectIntent(question);

        if (intent == QueryIntent.UNKNOWN) {
            throw new IllegalArgumentException(
                    "Could not determine query intent for: " +
                    question
            );
        }

        /*
         * Architecture queries currently do not require
         * an entity, but QueryExecutor does not yet support
         * architecture execution.
         *
         * Therefore this will be handled explicitly rather
         * than attempting entity resolution.
         */
        if (intent == QueryIntent.ARCHITECTURE) {
            throw new UnsupportedOperationException(
                    "Architecture queries are not yet supported " +
                    "by the natural-language query executor."
            );
        }

        /*
         * Step 2: Extract the entity mentioned in the question.
         */
        String entityName =
                extractEntityName(question, intent);

        if (entityName == null || entityName.isBlank()) {
            throw new IllegalArgumentException(
                    "Could not identify an entity in question: " +
                    question
            );
        }

        /*
         * Step 3: Create the QueryRequest.
         */
        QueryRequest request =
                new QueryRequest(
                        question,
                        intent,
                        entityName
                );

        /*
         * Step 4: Resolve the entity against the graph.
         */
        List<GraphNode> matches =
                entityResolver.resolve(
                        request.getEntityName()
                );

        if (matches.isEmpty()) {
            throw new IllegalArgumentException(
                    "Could not resolve entity: " +
                    request.getEntityName()
            );
        }

        if (matches.size() > 1) {
            throw new IllegalArgumentException(
                    "Ambiguous entity '" +
                    request.getEntityName() +
                    "'. Multiple graph nodes matched."
            );
        }

        GraphNode entity = matches.get(0);

        /*
         * Step 5: Create the query plan.
         */
        QueryPlan plan =
                queryPlanner.plan(
                        request.getIntent(),
                        entity
                );

        /*
         * Step 6: Execute the plan.
         */
        return queryExecutor.execute(
                plan,
                request.getOriginalQuestion()
        );
    }

    /**
     * Extracts the graph entity from the natural-language
     * question using deterministic patterns.
     *
     * Examples:
     *
     * "What does UserController depend on?"
     *     → UserController
     *
     * "Who depends on UserService?"
     *     → UserService
     *
     * "What methods does UserService call?"
     *     → UserService
     *
     * "Who calls UserRepository.save?"
     *     → UserRepository.save
     */
    private String extractEntityName(
            String question,
            QueryIntent intent) {

        String normalized = question.trim();

        switch (intent) {

            case DEPENDENCIES:
                return extractUsingPattern(
                        normalized,
                        "what does (.+?) depend on"
                );

            case DEPENDENTS:
                return extractUsingPattern(
                        normalized,
                        "who depends on (.+)"
                );

            case CALLEES:
                return extractUsingPattern(
                        normalized,
                        "what methods does (.+?) call"
                );

            case CALLERS:
                return extractUsingPattern(
                        normalized,
                        "who calls (.+)"
                );

            case SUBCLASSES:
                return extractUsingPattern(
                        normalized,
                        "subclasses of (.+)"
                );

            case SUPERCLASS:
                return extractUsingPattern(
                        normalized,
                        "superclass of (.+)"
                );

            case IMPLEMENTED_INTERFACES:
                return extractUsingPattern(
                        normalized,
                        "what interfaces does (.+?) implement"
                );

            case IMPLEMENTATIONS:
                return extractUsingPattern(
                        normalized,
                        "what implements (.+)"
                );

            case IMPACT:
                return extractImpactEntity(normalized);

            case REACHABILITY:
                return extractReachabilityEntity(normalized);

            default:
                return null;
        }
    }

    /**
     * Extracts an entity using a case-insensitive regex.
     */
    private String extractUsingPattern(
            String question,
            String regex) {

        Pattern pattern =
                Pattern.compile(
                        regex,
                        Pattern.CASE_INSENSITIVE
                );

        Matcher matcher =
                pattern.matcher(question);

        if (!matcher.find()) {
            return null;
        }

        return matcher.group(1)
                .trim()
                .replaceAll("[?.!]+$", "");
    }

    /**
     * Extracts an entity from impact questions.
     *
     * Examples:
     * "What would be affected if UserRepository changes?"
     * "What breaks if UserService changes?"
     */
    private String extractImpactEntity(String question) {

        String result = extractUsingPattern(
                question,
                "if (.+?) (?:changes|change)"
        );

        if (result != null) {
            return result;
        }

        return extractUsingPattern(
                question,
                "impact of changing (.+)"
        );
    }

    /**
     * Extracts an entity from reachability questions.
     *
     * Example:
     * "What is reachable from UserController?"
     */
    private String extractReachabilityEntity(String question) {

        return extractUsingPattern(
                question,
                "reachable from (.+)"
        );
    }
}