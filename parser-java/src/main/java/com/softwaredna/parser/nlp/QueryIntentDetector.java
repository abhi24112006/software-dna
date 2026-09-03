package com.softwaredna.parser.nlp;

import java.util.Locale;

/**
 * Detects the intent of a natural-language Software DNA query.
 *
 * This is the initial deterministic NLP layer.
 * The LLM layer will be added after this foundation is verified.
 */
public class QueryIntentDetector {

    /**
     * Detects the intent of a natural-language question.
     *
     * @param question natural-language question
     * @return detected QueryIntent
     */
    public QueryIntent detectIntent(String question) {

        if (question == null || question.trim().isEmpty()) {
            return QueryIntent.UNKNOWN;
        }

        String normalized = question
                .trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");

        // Architecture
        if (containsAny(normalized,
                "what is the architecture",
                "what architecture",
                "which architecture",
                "architecture style",
                "architectural style")) {
            return QueryIntent.ARCHITECTURE;
        }

        // Impact
        if (containsAny(normalized,
                "what would be affected",
                "what will be affected",
                "what could be affected",
                "what is affected",
                "what happens if",
                "impact of changing",
                "impact if",
                "what breaks if")) {
            return QueryIntent.IMPACT;
        }

        // Reachability
        if (containsAny(normalized,
                "what is reachable from",
                "what can be reached from",
                "reachable from",
                "path from",
                "paths from")) {
            return QueryIntent.REACHABILITY;
        }

        // Callers
        if (containsAny(normalized,
                "who calls",
                "which methods call",
                "callers of",
                "called by")) {
            return QueryIntent.CALLERS;
        }

        // Callees
        if (containsAny(normalized,
                "what calls",
                "what does",
                "what methods does",
                "which methods does",
                "callees of",
                "methods called by")) {

            if (normalized.contains("call")
                    || normalized.contains("callee")
                    || normalized.contains("called")) {
                return QueryIntent.CALLEES;
            }
        }

        // Implementations
        if (containsAny(normalized,
                "what implements",
                "which classes implement",
                "which class implements",
                "implementations of",
                "classes implementing")) {
            return QueryIntent.IMPLEMENTATIONS;
        }

        // Implemented interfaces
        if (containsAny(normalized,
                "what interfaces does",
                "which interfaces does",
                "interfaces implemented by",
                "interfaces implemented")) {
            return QueryIntent.IMPLEMENTED_INTERFACES;
        }

        // Subclasses
        if (containsAny(normalized,
                "what subclasses",
                "which subclasses",
                "subclasses of",
                "classes extending",
                "which classes extend")) {
            return QueryIntent.SUBCLASSES;
        }

        // Superclass
        if (containsAny(normalized,
                "what is the superclass",
                "what superclass",
                "which superclass",
                "superclass of",
                "parent class of")) {
            return QueryIntent.SUPERCLASS;
        }

        // Dependents
        if (containsAny(normalized,
                "who depends on",
                "which classes depend on",
                "which class depends on",
                "dependents of",
                "dependent on")) {
            return QueryIntent.DEPENDENTS;
        }

        // Dependencies
        if (containsAny(normalized,
                "what does",
                "what do",
                "dependencies of",
                "what are the dependencies of",
                "which dependencies does")) {

            if (normalized.contains("depend")
                    || normalized.contains("dependencies")) {
                return QueryIntent.DEPENDENCIES;
            }
        }

        return QueryIntent.UNKNOWN;
    }

    /**
     * Checks whether a question contains any supplied phrase.
     */
    private boolean containsAny(String question, String... phrases) {

        for (String phrase : phrases) {
            if (question.contains(phrase)) {
                return true;
            }
        }

        return false;
    }
}