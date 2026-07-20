package com.softwaredna.scope;

import java.util.HashMap;
import java.util.Map;

public class Scope {

    private final Map<String, String> variables =
            new HashMap<>();

    /**
     * Registers a variable in the current scope.
     *
     * Example:
     * teacher -> Teacher
     */
    public void declareVariable(
            String variableName,
            String typeName) {

        if (variableName == null || variableName.isBlank()) {
            return;
        }

        if (typeName == null || typeName.isBlank()) {
            return;
        }

        variables.put(variableName, typeName);

    }

    /**
     * Returns the declared type of a variable.
     *
     * Example:
     * teacher -> Teacher
     */
    public String resolveVariable(
            String variableName) {

        return variables.get(variableName);

    }

    /**
     * Checks whether a variable exists.
     */
    public boolean containsVariable(
            String variableName) {

        return variables.containsKey(variableName);

    }

    /**
     * Removes all variables from this scope.
     */
    public void clear() {

        variables.clear();

    }

}