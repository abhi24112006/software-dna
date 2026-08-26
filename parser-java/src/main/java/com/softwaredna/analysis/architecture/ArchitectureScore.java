package com.softwaredna.analysis.architecture;

public class ArchitectureScore {

    private final String architectureStyle;

    private final double score;

    private final String explanation;


    public ArchitectureScore(
            String architectureStyle,
            double score,
            String explanation) {

        this.architectureStyle =
                architectureStyle;

        this.score =
                score;

        this.explanation =
                explanation;

    }


    public String getArchitectureStyle() {

        return architectureStyle;

    }


    public double getScore() {

        return score;

    }


    public String getExplanation() {

        return explanation;

    }

}