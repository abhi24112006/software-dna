package com.softwaredna.parser.nlp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class QueryIntentDetectorTest {

    private final QueryIntentDetector detector = new QueryIntentDetector();

    @Test
    void shouldDetectDependencies() {
        assertEquals(
                QueryIntent.DEPENDENCIES,
                detector.detectIntent(
                        "What does UserController depend on?"
                )
        );
    }

    @Test
    void shouldDetectDependents() {
        assertEquals(
                QueryIntent.DEPENDENTS,
                detector.detectIntent(
                        "Who depends on UserRepository?"
                )
        );
    }

    @Test
    void shouldDetectCallees() {
        assertEquals(
                QueryIntent.CALLEES,
                detector.detectIntent(
                        "What methods does UserService call?"
                )
        );
    }

    @Test
    void shouldDetectCallers() {
        assertEquals(
                QueryIntent.CALLERS,
                detector.detectIntent(
                        "Who calls UserRepository.save?"
                )
        );
    }

    @Test
    void shouldDetectSubclasses() {
        assertEquals(
                QueryIntent.SUBCLASSES,
                detector.detectIntent(
                        "What subclasses does UserService have?"
                )
        );
    }

    @Test
    void shouldDetectSuperclass() {
        assertEquals(
                QueryIntent.SUPERCLASS,
                detector.detectIntent(
                        "What is the superclass of UserService?"
                )
        );
    }

    @Test
    void shouldDetectImplementedInterfaces() {
        assertEquals(
                QueryIntent.IMPLEMENTED_INTERFACES,
                detector.detectIntent(
                        "What interfaces does UserController implement?"
                )
        );
    }

    @Test
    void shouldDetectImplementations() {
        assertEquals(
                QueryIntent.IMPLEMENTATIONS,
                detector.detectIntent(
                        "What implements UserRepository?"
                )
        );
    }

    @Test
    void shouldDetectImpact() {
        assertEquals(
                QueryIntent.IMPACT,
                detector.detectIntent(
                        "What would be affected if UserRepository changes?"
                )
        );
    }

    @Test
    void shouldDetectReachability() {
        assertEquals(
                QueryIntent.REACHABILITY,
                detector.detectIntent(
                        "What is reachable from UserController?"
                )
        );
    }

    @Test
    void shouldDetectArchitecture() {
        assertEquals(
                QueryIntent.ARCHITECTURE,
                detector.detectIntent(
                        "What is the architecture of this repository?"
                )
        );
    }

    @Test
    void shouldDetectMetricsFromCyclomaticComplexityQuestion() {
        assertEquals(
                QueryIntent.METRICS,
                detector.detectIntent(
                        "What is the cyclomatic complexity of UserService?"
                )
        );
    }

    @Test
    void shouldDetectMetricsFromLocQuestion() {
        assertEquals(
                QueryIntent.METRICS,
                detector.detectIntent(
                        "What is the LOC of UserController?"
                )
        );
    }

    @Test
    void shouldDetectMetricsFromGeneralMetricsQuestion() {
        assertEquals(
                QueryIntent.METRICS,
                detector.detectIntent(
                        "What are the metrics of UserService?"
                )
        );
    }

    @Test
    void shouldDetectMetricsFromFanInQuestion() {
        assertEquals(
                QueryIntent.METRICS,
                detector.detectIntent(
                        "What is the fan-in of UserRepository?"
                )
        );
    }

    @Test
    void shouldDetectMetricsFromMethodCountQuestion() {
        assertEquals(
                QueryIntent.METRICS,
                detector.detectIntent(
                        "How many methods does UserService have?"
                )
        );
    }

    @Test
    void shouldReturnUnknownForUnsupportedQuestion() {
        assertEquals(
                QueryIntent.UNKNOWN,
                detector.detectIntent(
                        "What is the weather today?"
                )
        );
    }

    @Test
    void shouldReturnUnknownForNullQuestion() {
        assertEquals(
                QueryIntent.UNKNOWN,
                detector.detectIntent(null)
        );
    }

    @Test
    void shouldReturnUnknownForEmptyQuestion() {
        assertEquals(
                QueryIntent.UNKNOWN,
                detector.detectIntent("   ")
        );
    }
}