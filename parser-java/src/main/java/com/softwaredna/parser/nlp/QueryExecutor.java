package com.softwaredna.parser.nlp;

import java.util.List;

import com.softwaredna.analysis.metrics.MetricQueryService;
import com.softwaredna.knowledge.GraphNode;
import com.softwaredna.knowledge.NodeType;
import com.softwaredna.knowledge.Subgraph;
import com.softwaredna.knowledge.SubgraphRetriever;
import com.softwaredna.knowledge.query.ImpactAnalyzer;
import com.softwaredna.knowledge.query.KnowledgeGraphQuery;
import com.softwaredna.model.ClassMetrics;

/**
 * Executes a QueryPlan against the existing Knowledge Graph
 * query layer.
 *
 * Metric queries are handled through MetricQueryService because
 * metric data is stored in the normalized RepositoryModel rather
 * than in the Knowledge Graph.
 *
 * Structural query results can also carry a focused Subgraph
 * retrieved around the target entity.
 */
public class QueryExecutor {

    private final KnowledgeGraphQuery graphQuery;
    private final ImpactAnalyzer impactAnalyzer;
    private final MetricQueryService metricQueryService;
    private final SubgraphRetriever subgraphRetriever;

    /**
     * Backward-compatible constructor for structural graph queries.
     */
    public QueryExecutor(KnowledgeGraphQuery graphQuery) {
        this(graphQuery, null, null);
    }

    /**
     * Constructor that also enables metric queries.
     */
    public QueryExecutor(
            KnowledgeGraphQuery graphQuery,
            MetricQueryService metricQueryService) {

        this(graphQuery, metricQueryService, null);
    }

    /**
     * Constructor that enables metric queries and subgraph retrieval.
     */
    public QueryExecutor(
            KnowledgeGraphQuery graphQuery,
            MetricQueryService metricQueryService,
            SubgraphRetriever subgraphRetriever) {

        if (graphQuery == null) {
            throw new IllegalArgumentException(
                    "KnowledgeGraphQuery cannot be null."
            );
        }

        this.graphQuery = graphQuery;
        this.impactAnalyzer =
                new ImpactAnalyzer(graphQuery);
        this.metricQueryService = metricQueryService;
        this.subgraphRetriever = subgraphRetriever;
    }

    public QueryResult execute(
            QueryPlan plan,
            String originalQuestion) {

        if (plan == null) {
            throw new IllegalArgumentException(
                    "QueryPlan cannot be null."
            );
        }

        if (originalQuestion == null
                || originalQuestion.isBlank()) {

            throw new IllegalArgumentException(
                    "Original question cannot be null or blank."
            );
        }

        GraphNode entity =
                plan.getEntity();

        String entityId =
                entity.getId();

        List<GraphNode> nodes;
        ClassMetrics metrics = null;

        switch (plan.getOperation()) {

            case GET_DEPENDENCIES:

                nodes =
                        graphQuery.getDependencies(
                                entityId
                        );

                break;

            case GET_DEPENDENTS:

                nodes =
                        graphQuery.getDependents(
                                entityId
                        );

                break;

            case GET_CALLEES:

                if (entity.getType() == NodeType.CLASS) {

                    nodes =
                            graphQuery.getClassCallees(
                                    entityId
                            );

                } else {

                    nodes =
                            graphQuery.getCallees(
                                    entityId
                            );
                }

                break;

            case GET_CALLERS:

                nodes =
                        graphQuery.getCallers(
                                entityId
                        );

                break;

            case GET_SUBCLASSES:

                nodes =
                        graphQuery.getSubclasses(
                                entityId
                        );

                break;

            case GET_SUPERCLASS:

                nodes =
                        graphQuery.getSuperclass(
                                entityId
                        );

                break;

            case GET_IMPLEMENTED_INTERFACES:

                nodes =
                        graphQuery.getImplementedInterfaces(
                                entityId
                        );

                break;

            case GET_IMPLEMENTATIONS:

                nodes =
                        graphQuery.getImplementations(
                                entityId
                        );

                break;

            case GET_IMPACT:

                nodes =
                        impactAnalyzer.getContainmentAwareImpact(
                                entityId
                        );

                break;

            case GET_REACHABILITY:

                nodes =
                        graphQuery.getReachableNodes(
                                entityId
                        );

                break;

            case GET_METRICS:

                if (metricQueryService == null) {
                    throw new IllegalStateException(
                            "MetricQueryService is required "
                                    + "for GET_METRICS queries."
                    );
                }

                if (entity.getType() != NodeType.CLASS) {
                    throw new IllegalArgumentException(
                            "Metrics are currently supported "
                                    + "only for class entities."
                    );
                }

                metrics =
                        metricQueryService.getClassMetrics(
                                entity.getName()
                        );

                nodes = List.of();

                break;

            case GET_ARCHITECTURE:
            case NONE:

            default:

                throw new UnsupportedOperationException(
                        "Query operation is not yet supported by "
                                + "KnowledgeGraphQuery: "
                                + plan.getOperation()
                );
        }

        Subgraph subgraph = null;

        if (subgraphRetriever != null) {
            subgraph =
                    subgraphRetriever.retrieve(entity);
        }

        return new QueryResult(
                originalQuestion,
                plan.getIntent(),
                entity,
                nodes,
                metrics,
                subgraph
        );
    }

    /**
     * Backward-compatible convenience method.
     */
    public QueryResult execute(
            QueryPlan plan) {

        if (plan == null) {
            throw new IllegalArgumentException(
                    "QueryPlan cannot be null."
            );
        }

        return execute(
                plan,
                plan.getIntent()
                        + " query for "
                        + plan.getEntity().getName()
        );
    }
}