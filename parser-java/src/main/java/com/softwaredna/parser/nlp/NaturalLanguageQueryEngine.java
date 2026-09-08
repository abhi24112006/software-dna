package com.softwaredna.parser.nlp;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.softwaredna.analysis.architecture.ArchitectureAnalyzer;
import com.softwaredna.analysis.architecture.ArchitectureReport;
import com.softwaredna.graph.GraphRepository;
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
 *
 * Architecture analysis is optionally connected through
 * GraphRepository and ArchitectureAnalyzer.
 */
public class NaturalLanguageQueryEngine {

    private final QueryIntentDetector intentDetector;
    private final EntityResolver entityResolver;
    private final QueryPlanner queryPlanner;
    private final QueryExecutor queryExecutor;
    private final AnswerGenerator answerGenerator;
    private final LLMAnswerGenerator llmAnswerGenerator;

    /*
     * Optional architecture-analysis dependencies.
     *
     * These are null when the engine is created using
     * the original constructors.
     */
    private final GraphRepository graphRepository;
    private final ArchitectureAnalyzer architectureAnalyzer;

    /**
     * Creates a natural-language query engine using
     * deterministic answer generation.
     *
     * @param graph Knowledge Graph to query
     */
    public NaturalLanguageQueryEngine(KnowledgeGraph graph) {
        this(graph, null, null);
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

        this(graph, null, llmClient);
    }

    /**
     * Creates a natural-language query engine with optional
     * architecture-analysis support.
     *
     * @param graph Knowledge Graph to query
     * @param graphRepository repository used for architecture analysis
     */
    public NaturalLanguageQueryEngine(
            KnowledgeGraph graph,
            GraphRepository graphRepository) {

        this(graph, graphRepository, null);
    }

    /**
     * Creates a natural-language query engine with optional
     * architecture-analysis and LLM support.
     *
     * @param graph Knowledge Graph to query
     * @param graphRepository repository used for architecture analysis
     * @param llmClient optional LLM client
     */
    public NaturalLanguageQueryEngine(
            KnowledgeGraph graph,
            GraphRepository graphRepository,
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

        this.graphRepository = graphRepository;

        if (graphRepository != null) {
            this.architectureAnalyzer =
                    new ArchitectureAnalyzer(graphRepository);
        } else {
            this.architectureAnalyzer = null;
        }

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
     * Architecture queries are handled separately through
     * askArchitecture(String), because architecture analysis
     * produces an ArchitectureReport rather than a QueryResult.
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
                    "Architecture queries return an ArchitectureReport. " +
                    "Use askArchitecture(String) instead."
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
     * Analyzes the architecture of the repository.
     *
     * The architecture analyzer uses the GraphRepository as
     * its source of graph information and analyzes all class nodes.
     *
     * @return architecture analysis report
     * @throws IllegalStateException if architecture analysis was
     *                               not configured
     */
    public ArchitectureReport analyzeArchitecture() {

        if (architectureAnalyzer == null ||
                graphRepository == null) {

            throw new IllegalStateException(
                    "Architecture analysis is not configured. " +
                    "Create the engine with a GraphRepository."
            );
        }

        List<String> classNodes =
                graphRepository.getClassNodes();

        return architectureAnalyzer.analyze(classNodes);
    }

    /**
     * Processes an architecture-related natural-language question
     * and returns the architecture analysis report.
     *
     * The question must be detected as an ARCHITECTURE intent.
     *
     * @param question architecture-related natural-language question
     * @return ArchitectureReport containing the recovered architecture
     */
    public ArchitectureReport askArchitecture(String question) {

        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException(
                    "Question cannot be null or blank."
            );
        }

        QueryIntent intent =
                intentDetector.detectIntent(question);

        if (intent != QueryIntent.ARCHITECTURE) {
            throw new IllegalArgumentException(
                    "Question is not an architecture query: " +
                    question
            );
        }

        return analyzeArchitecture();
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
     * @param question question text
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
     * @param question question text
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
     * @param question question text
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
     * Supports forms such as:
     *
     * "What is reachable from UserController?"
     * "What can be reached from UserController?"
     * "What can UserController reach?"
     * "What does UserController reach?"
     *
     * @param question question text
     * @return entity name
     */
    private String extractReachabilityEntity(String question) {

        String result = extractUsingPattern(
                question,
                "reachable from (.+)"
        );

        if (result != null) {
            return result;
        }

        result = extractUsingPattern(
                question,
                "what can be reached from (.+)"
        );

        if (result != null) {
            return result;
        }

        result = extractUsingPattern(
                question,
                "what can (.+?) reach"
        );

        if (result != null) {
            return result;
        }

        return extractUsingPattern(
                question,
                "what does (.+?) reach"
        );
    }
}