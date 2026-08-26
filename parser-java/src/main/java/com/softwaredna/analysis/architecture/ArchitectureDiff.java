package com.softwaredna.analysis.architecture;

import java.util.Collections;
import java.util.List;

public class ArchitectureDiff {

    private final String previousStyle;

    private final String currentStyle;

    private final double previousHealth;

    private final double currentHealth;

    private final List<String> addedDependencies;

    private final List<String> removedDependencies;

    private final List<String> newAnomalies;

    private final List<String> resolvedAnomalies;


    public ArchitectureDiff(
            String previousStyle,
            String currentStyle,
            double previousHealth,
            double currentHealth,
            List<String> addedDependencies,
            List<String> removedDependencies,
            List<String> newAnomalies,
            List<String> resolvedAnomalies) {

        this.previousStyle =
                previousStyle;

        this.currentStyle =
                currentStyle;

        this.previousHealth =
                previousHealth;

        this.currentHealth =
                currentHealth;

        this.addedDependencies =
                Collections.unmodifiableList(
                        addedDependencies
                );

        this.removedDependencies =
                Collections.unmodifiableList(
                        removedDependencies
                );

        this.newAnomalies =
                Collections.unmodifiableList(
                        newAnomalies
                );

        this.resolvedAnomalies =
                Collections.unmodifiableList(
                        resolvedAnomalies
                );

    }


    public String getPreviousStyle() {

        return previousStyle;

    }


    public String getCurrentStyle() {

        return currentStyle;

    }


    public double getPreviousHealth() {

        return previousHealth;

    }


    public double getCurrentHealth() {

        return currentHealth;

    }


    public List<String>
    getAddedDependencies() {

        return addedDependencies;

    }


    public List<String>
    getRemovedDependencies() {

        return removedDependencies;

    }


    public List<String>
    getNewAnomalies() {

        return newAnomalies;

    }


    public List<String>
    getResolvedAnomalies() {

        return resolvedAnomalies;

    }


    public double getHealthChange() {

        return currentHealth
                - previousHealth;

    }

}