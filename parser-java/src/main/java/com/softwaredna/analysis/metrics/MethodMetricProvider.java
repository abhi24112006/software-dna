package com.softwaredna.analysis.metrics;

import com.softwaredna.model.MethodMetrics;

/**
 * Language-independent contract for calculating method-level metrics.
 *
 * <p>The input type is generic because each programming language
 * has its own AST or intermediate representation. The output is
 * always the common Software DNA {@link MethodMetrics} model.</p>
 *
 * @param <T> language-specific method representation
 */
public interface MethodMetricProvider<T> {

    /**
     * Calculates the common method metrics for a language-specific
     * method representation.
     *
     * @param method language-specific method representation
     * @return common method metrics
     */
    MethodMetrics calculate(T method);
}