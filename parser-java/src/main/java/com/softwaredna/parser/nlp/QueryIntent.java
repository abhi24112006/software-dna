package com.softwaredna.parser.nlp;

/**
 * Represents the type of question asked by the user.
 *
 * The NLP layer uses this intent to determine which
 * Software DNA graph operation should be executed.
 */
public enum QueryIntent {

    /**
     * Find entities that the target entity depends on.
     */
    DEPENDENCIES,

    /**
     * Find entities that depend on the target entity.
     */
    DEPENDENTS,

    /**
     * Find methods called by the target method.
     */
    CALLEES,

    /**
     * Find methods that call the target method.
     */
    CALLERS,

    /**
     * Find subclasses of the target class.
     */
    SUBCLASSES,

    /**
     * Find the superclass of the target class.
     */
    SUPERCLASS,

    /**
     * Find interfaces implemented by the target class.
     */
    IMPLEMENTED_INTERFACES,

    /**
     * Find classes that implement the target interface.
     */
    IMPLEMENTATIONS,

    /**
     * Find entities potentially affected by changing the target.
     */
    IMPACT,

    /**
     * Find entities reachable from the target.
     */
    REACHABILITY,

    /**
     * Explain or identify the recovered architecture.
     */
    ARCHITECTURE,

    /**
     * Question could not be mapped to a supported intent.
     */
    UNKNOWN
}