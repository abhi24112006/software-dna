package com.softwaredna.analysis.architecture;

public class ArchitectureTrend {

    private final double startingHealth;

    private final double currentHealth;

    private final int totalChanges;

    private final int totalNewAnomalies;

    private final int totalResolvedAnomalies;

    private final int totalAddedDependencies;

    private final int totalRemovedDependencies;


    public ArchitectureTrend(
            double startingHealth,
            double currentHealth,
            int totalChanges,
            int totalNewAnomalies,
            int totalResolvedAnomalies,
            int totalAddedDependencies,
            int totalRemovedDependencies) {

        this.startingHealth =
                startingHealth;

        this.currentHealth =
                currentHealth;

        this.totalChanges =
                totalChanges;

        this.totalNewAnomalies =
                totalNewAnomalies;

        this.totalResolvedAnomalies =
                totalResolvedAnomalies;

        this.totalAddedDependencies =
                totalAddedDependencies;

        this.totalRemovedDependencies =
                totalRemovedDependencies;

    }


    public double getStartingHealth() {

        return startingHealth;

    }


    public double getCurrentHealth() {

        return currentHealth;

    }


    public double getHealthChange() {

        return currentHealth
                - startingHealth;

    }


    public int getTotalChanges() {

        return totalChanges;

    }


    public int getTotalNewAnomalies() {

        return totalNewAnomalies;

    }


    public int getTotalResolvedAnomalies() {

        return totalResolvedAnomalies;

    }


    public int getTotalAddedDependencies() {

        return totalAddedDependencies;

    }


    public int getTotalRemovedDependencies() {

        return totalRemovedDependencies;

    }

}