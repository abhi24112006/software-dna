package com.softwaredna.parser.nlp;

/**
 * Represents the graph operation that should be performed
 * for a particular natural-language query intent.
 *
 * This enum describes the operation only.
 * Actual graph traversal is handled by the existing
 * KnowledgeGraphQuery and related analysis components.
 */
public enum QueryOperation {

    GET_DEPENDENCIES,

    GET_DEPENDENTS,

    GET_CALLEES,

    GET_CALLERS,

    GET_SUBCLASSES,

    GET_SUPERCLASS,

    GET_IMPLEMENTED_INTERFACES,

    GET_IMPLEMENTATIONS,

    GET_IMPACT,

    GET_REACHABILITY,

    GET_ARCHITECTURE,

    NONE
}