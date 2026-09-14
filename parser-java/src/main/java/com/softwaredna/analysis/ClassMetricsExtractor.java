package com.softwaredna.analysis;

import com.softwaredna.analysis.metrics.ClassMetricAggregator;
import com.softwaredna.model.ClassMetrics;
import com.softwaredna.model.ParsedClass;

/**
 * Compatibility façade for the common class metric aggregator.
 *
 * <p>New code should use ClassMetricAggregator directly.
 * This class is retained so existing callers do not break
 * during the metric-engine migration.</p>
 */
@Deprecated
public class ClassMetricsExtractor {

    private final ClassMetricAggregator aggregator;

    public ClassMetricsExtractor() {

        aggregator =
                new ClassMetricAggregator();
    }

    public ClassMetrics extract(
            ParsedClass parsedClass) {

        return aggregator.aggregate(
                parsedClass
        );
    }
}