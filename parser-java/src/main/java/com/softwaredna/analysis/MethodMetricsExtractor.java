package com.softwaredna.analysis;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.softwaredna.analysis.metrics.java.JavaMethodMetricProvider;
import com.softwaredna.model.MethodMetrics;

/**
 * Compatibility façade for the Java method metric provider.
 *
 * <p>New code should use {@link JavaMethodMetricProvider}
 * through the common MethodMetricProvider abstraction.</p>
 *
 * <p>This class remains temporarily so existing callers
 * continue to work during the metric-engine migration.</p>
 */
@Deprecated
public class MethodMetricsExtractor {

    private final JavaMethodMetricProvider provider;

    public MethodMetricsExtractor() {

        provider =
                new JavaMethodMetricProvider();
    }

    /**
     * Extracts method metrics using the Java metric provider.
     *
     * @param method JavaParser method declaration
     * @return common method metrics
     */
    public MethodMetrics extract(
            MethodDeclaration method) {

        return provider.calculate(
                method
        );
    }
}