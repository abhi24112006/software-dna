package com.softwaredna.analysis.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.softwaredna.model.MethodMetrics;

class CrossLanguageMetricContractTest {

    @Test
    void shouldExposeCommonMethodMetricContract() {

        MethodMetrics metrics =
                new MethodMetrics();

        metrics.setParameterCount(2);
        metrics.setLocalVariableCount(3);
        metrics.setMethodCallCount(4);
        metrics.setObjectCreationCount(1);
        metrics.setReturnCount(2);
        metrics.setLoopCount(1);
        metrics.setConditionalCount(2);
        metrics.setCyclomaticComplexity(4);
        metrics.setMaximumNestingDepth(2);
        metrics.setLinesOfCode(10);

        assertNotNull(metrics);

        assertEquals(
                2,
                metrics.getParameterCount()
        );

        assertEquals(
                3,
                metrics.getLocalVariableCount()
        );

        assertEquals(
                4,
                metrics.getMethodCallCount()
        );

        assertEquals(
                1,
                metrics.getObjectCreationCount()
        );

        assertEquals(
                2,
                metrics.getReturnCount()
        );

        assertEquals(
                1,
                metrics.getLoopCount()
        );

        assertEquals(
                2,
                metrics.getConditionalCount()
        );

        assertEquals(
                4,
                metrics.getCyclomaticComplexity()
        );

        assertEquals(
                2,
                metrics.getMaximumNestingDepth()
        );

        assertEquals(
                10,
                metrics.getLinesOfCode()
        );
    }

    @Test
    void shouldUseZeroAsDefaultForMetricCounts() {

        MethodMetrics metrics =
                new MethodMetrics();

        assertEquals(
                0,
                metrics.getParameterCount()
        );

        assertEquals(
                0,
                metrics.getLocalVariableCount()
        );

        assertEquals(
                0,
                metrics.getMethodCallCount()
        );

        assertEquals(
                0,
                metrics.getObjectCreationCount()
        );

        assertEquals(
                0,
                metrics.getReturnCount()
        );

        assertEquals(
                0,
                metrics.getLoopCount()
        );

        assertEquals(
                0,
                metrics.getConditionalCount()
        );

        assertEquals(
                0,
                metrics.getCyclomaticComplexity()
        );

        assertEquals(
                0,
                metrics.getMaximumNestingDepth()
        );

        assertEquals(
                0,
                metrics.getLinesOfCode()
        );
    }
}