package com.softwaredna.model;

import java.util.Objects;

/**
 * Identifies the source location that produced a relationship.
 *
 * <p>Source evidence allows relationships in the Software DNA graph
 * to be traced back to the source code that caused them.</p>
 */
public class SourceEvidence {

    private String filePath;

    private int lineNumber;

    public SourceEvidence() {

    }

    public SourceEvidence(
            String filePath,
            int lineNumber) {

        this.filePath = filePath;
        this.lineNumber = lineNumber;
    }

    public String getFilePath() {

        return filePath;
    }

    public void setFilePath(
            String filePath) {

        this.filePath = filePath;
    }

    public int getLineNumber() {

        return lineNumber;
    }

    public void setLineNumber(
            int lineNumber) {

        this.lineNumber = lineNumber;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null
                || getClass() != obj.getClass()) {

            return false;
        }

        SourceEvidence other =
                (SourceEvidence) obj;

        return lineNumber == other.lineNumber
                && Objects.equals(
                        filePath,
                        other.filePath
                );
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                filePath,
                lineNumber
        );
    }
}