package com.softwaredna.analysis.architecture;

public class ArchitectureRecommendation {

    private final String title;

    private final String severity;

    private final String problem;

    private final String reason;

    private final String suggestedAction;

    private final String expectedBenefit;


    public ArchitectureRecommendation(
            String title,
            String severity,
            String problem,
            String reason,
            String suggestedAction,
            String expectedBenefit) {

        this.title =
                title;

        this.severity =
                severity;

        this.problem =
                problem;

        this.reason =
                reason;

        this.suggestedAction =
                suggestedAction;

        this.expectedBenefit =
                expectedBenefit;

    }


    public String getTitle() {

        return title;

    }


    public String getSeverity() {

        return severity;

    }


    public String getProblem() {

        return problem;

    }


    public String getReason() {

        return reason;

    }


    public String getSuggestedAction() {

        return suggestedAction;

    }


    public String getExpectedBenefit() {

        return expectedBenefit;

    }


    public void print() {

        System.out.println();

        System.out.println(
                "["
                        + severity
                        + "] "
                        + title
        );

        System.out.println(
                "  Problem:"
        );

        System.out.println(
                "    "
                        + problem
        );

        System.out.println(
                "  Why it matters:"
        );

        System.out.println(
                "    "
                        + reason
        );

        System.out.println(
                "  Suggested action:"
        );

        System.out.println(
                "    "
                        + suggestedAction
        );

        System.out.println(
                "  Expected benefit:"
        );

        System.out.println(
                "    "
                        + expectedBenefit
        );

    }

}