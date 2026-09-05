package com.softwaredna.parser.nlp;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.KnowledgeGraph;
import com.softwaredna.knowledge.NodeType;
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
 *        ↓
 * AnswerGenerator
 *        ↓
 * Natural-language answer
 *
 * The graph remains the source of truth.
 * AnswerGenerator only converts graph-derived results
 * into a human-readable answer.
 */
public class NaturalLanguageQueryEngine {

    private final QueryIntentDetector intentDetector;
    private final EntityResolver entityResolver;
    private final QueryPlanner queryPlanner;
    private final QueryExecutor queryExecutor;
    private final AnswerGenerator answerGenerator;
    private final LLMAnswerGenerator llmAnswerGenerator;

    /**
     * Creates a natural-language query engine using
     * deterministic answer generation.
     *
     * @param graph Knowledge Graph to query
     */
    public NaturalLanguageQueryEngine(KnowledgeGraph graph) {

        this(graph, null);
    }

    /**
     * Creates a natural-language query engine with optional
     * LLM-backed answer generation.
     *
     * If the LLM client is null, deterministic answer generation
     * remains the default.
     *
     * @param graph Knowledge Graph to query
     * @param llmClient optional LLM client
     */
    public NaturalLanguageQueryEngine(
            KnowledgeGraph graph,
            LLMClient llmClient) {

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
        this.answerGenerator = new AnswerGenerator();

        if (llmClient != null) {
            this.llmAnswerGenerator =
                    new LLMAnswerGenerator(llmClient);
        } else {
            this.llmAnswerGenerator = null;
        }
    }

    /**
     * Processes a natural-language question and returns
     * the structured graph-derived result.
     *
     * @param question natural-language question
     * @return QueryResult containing graph-derived facts
     */
    public QueryResult ask(String question) {

        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException(
                    "Question cannot be null or blank."
            );
        }

        QueryIntent intent =
                intentDetector.detectIntent(question);

        if (intent == QueryIntent.UNKNOWN) {
            throw new IllegalArgumentException(
                    "Could not determine query intent for: " +
                    question
            );
        }

        if (intent == QueryIntent.ARCHITECTURE) {
            throw new UnsupportedOperationException(
                    "Architecture queries are not yet supported " +
                    "by the natural-language query executor."
            );
        }

        String entityName =
                extractEntityName(question, intent);

        if (entityName == null || entityName.isBlank()) {
            throw new IllegalArgumentException(
                    "Could not identify an entity in question: " +
                    question
            );
        }

        QueryRequest request =
                new QueryRequest(
                        question,
                        intent,
                        entityName
                );

        NodeType expectedType =
                getExpectedNodeType(intent);

        List<GraphNode> matches;

        if (expectedType != null) {
            matches =
                    entityResolver.resolve(
                            request.getEntityName(),
                            expectedType
                    );
        } else {
            matches =
                    entityResolver.resolve(
                            request.getEntityName()
                    );
        }

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

        QueryPlan plan =
                queryPlanner.plan(
                        request.getIntent(),
                        entity
                );

        return queryExecutor.execute(
                plan,
                request.getOriginalQuestion()
        );
    }

    /**
     * Determines the expected graph node type for a query intent.
     *
     * @param intent detected query intent
     * @return expected NodeType, or null when no specific type is required
     */
    private NodeType getExpectedNodeType(QueryIntent intent) {

        switch (intent) {

            case DEPENDENCIES:
                return NodeType.CLASS;

            case DEPENDENTS:
                return NodeType.CLASS;

            case CALLEES:
                return NodeType.CLASS;

            case CALLERS:
                return NodeType.METHOD;

            case SUBCLASSES:
                return NodeType.CLASS;

            case SUPERCLASS:
                return NodeType.CLASS;

            case IMPLEMENTED_INTERFACES:
                return NodeType.CLASS;

            case IMPLEMENTATIONS:
                return NodeType.INTERFACE;

            case IMPACT:
                return NodeType.CLASS;

            case REACHABILITY:
                return NodeType.CLASS;

            default:
                return null;
        }
    }

    /**
     * Generates a deterministic natural-language answer.
     *
     * This method preserves the original answer-generation behavior.
     *
     * @param question natural-language question
     * @return deterministic graph-based answer
     */
    public String askAndAnswer(String question) {

        QueryResult result = ask(question);

        return answerGenerator.generate(result);
    }

    /**
     * Generates an LLM-backed answer using only graph-derived
     * context.
     *
     * The deterministic AnswerGenerator remains available through
     * askAndAnswer(String).
     *
     * If the configured LLM fails, LLMAnswerGenerator falls back
     * to the deterministic AnswerGenerator.
     *
     * @param question natural-language question
     * @return grounded LLM-generated answer
     */
    public String askAndAnswerWithLLM(String question) {

        if (llmAnswerGenerator == null) {
            throw new IllegalStateException(
                    "No LLMClient has been configured."
            );
        }

        QueryResult result = ask(question);

        return llmAnswerGenerator.generate(result);
    }

    /**
     * Extracts the entity referenced by the question.
     *
     * @param question natural-language question
     * @param intent detected query intent
     * @return extracted entity name, or null if it cannot be extracted
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
     * Extracts an entity using a regular expression.
     *
     * @param question question text
     * @param regex extraction pattern
     * @return extracted entity name
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
     * Extracts the entity from an impact question.
     *
     * @param question question text
     * @return entity name
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
     * Extracts the starting entity from a reachability question.
     *
     * @param question question text
     * @return entity name
     */
    private String extractReachabilityEntity(String question) {

        return extractUsingPattern(
                question,
                "reachable from (.+)"
        );
    }
}