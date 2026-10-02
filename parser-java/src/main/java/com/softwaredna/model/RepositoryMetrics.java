package com.softwaredna.model;

/**
 * Repository-level software metrics.
 *
 * <p>This class stores aggregated metrics for an entire repository.
 * The values are calculated from the metrics and entities of the
 * parsed repository by the analysis layer.</p>
 */
public class RepositoryMetrics {

    // Repository structure
    private int totalFiles;
    private int totalClasses;
    private int totalInterfaces;
    private int totalEnums;
    private int totalRecords;
    private int totalMethods;
    private int totalFields;
    private int totalConstructors;

    // Method-level aggregates
    private int totalLinesOfCode;
    private double averageMethodLinesOfCode;
    private int totalParameters;
    private int totalLocalVariables;
    private int totalMethodCalls;
    private int totalObjectCreations;
    private int totalReturnStatements;

    // Complexity
    private int totalCyclomaticComplexity;
    private double averageCyclomaticComplexity;
    private int maximumCyclomaticComplexity;

    // Control-flow metrics
    private int totalLoops;
    private int totalConditionals;
    private int maximumNestingDepth;

    // Coupling and inheritance
    private double averageFanIn;
    private double averageFanOut;
    private double averageCbo;
    private int maximumDit;
    private int totalNoc;

    // Response for Class (RFC)
    private double averageRfc;

    // ---------------------------------------------------------
    // Repository structure
    // ---------------------------------------------------------

    public int getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(int totalFiles) {
        this.totalFiles = totalFiles;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }

    public int getTotalInterfaces() {
        return totalInterfaces;
    }

    public void setTotalInterfaces(int totalInterfaces) {
        this.totalInterfaces = totalInterfaces;
    }

    public int getTotalEnums() {
        return totalEnums;
    }

    public void setTotalEnums(int totalEnums) {
        this.totalEnums = totalEnums;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getTotalMethods() {
        return totalMethods;
    }

    public void setTotalMethods(int totalMethods) {
        this.totalMethods = totalMethods;
    }

    public int getTotalFields() {
        return totalFields;
    }

    public void setTotalFields(int totalFields) {
        this.totalFields = totalFields;
    }

    public int getTotalConstructors() {
        return totalConstructors;
    }

    public void setTotalConstructors(int totalConstructors) {
        this.totalConstructors = totalConstructors;
    }

    // ---------------------------------------------------------
    // Method-level aggregates
    // ---------------------------------------------------------

    public int getTotalLinesOfCode() {
        return totalLinesOfCode;
    }

    public void setTotalLinesOfCode(int totalLinesOfCode) {
        this.totalLinesOfCode = totalLinesOfCode;
    }

    public double getAverageMethodLinesOfCode() {
        return averageMethodLinesOfCode;
    }

    public void setAverageMethodLinesOfCode(double averageMethodLinesOfCode) {
        this.averageMethodLinesOfCode = averageMethodLinesOfCode;
    }

    public int getTotalParameters() {
        return totalParameters;
    }

    public void setTotalParameters(int totalParameters) {
        this.totalParameters = totalParameters;
    }

    public int getTotalLocalVariables() {
        return totalLocalVariables;
    }

    public void setTotalLocalVariables(int totalLocalVariables) {
        this.totalLocalVariables = totalLocalVariables;
    }

    public int getTotalMethodCalls() {
        return totalMethodCalls;
    }

    public void setTotalMethodCalls(int totalMethodCalls) {
        this.totalMethodCalls = totalMethodCalls;
    }

    public int getTotalObjectCreations() {
        return totalObjectCreations;
    }

    public void setTotalObjectCreations(int totalObjectCreations) {
        this.totalObjectCreations = totalObjectCreations;
    }

    public int getTotalReturnStatements() {
        return totalReturnStatements;
    }

    public void setTotalReturnStatements(int totalReturnStatements) {
        this.totalReturnStatements = totalReturnStatements;
    }

    // ---------------------------------------------------------
    // Complexity
    // ---------------------------------------------------------

    public int getTotalCyclomaticComplexity() {
        return totalCyclomaticComplexity;
    }

    public void setTotalCyclomaticComplexity(int totalCyclomaticComplexity) {
        this.totalCyclomaticComplexity = totalCyclomaticComplexity;
    }

    public double getAverageCyclomaticComplexity() {
        return averageCyclomaticComplexity;
    }

    public void setAverageCyclomaticComplexity(double averageCyclomaticComplexity) {
        this.averageCyclomaticComplexity = averageCyclomaticComplexity;
    }

    public int getMaximumCyclomaticComplexity() {
        return maximumCyclomaticComplexity;
    }

    public void setMaximumCyclomaticComplexity(int maximumCyclomaticComplexity) {
        this.maximumCyclomaticComplexity = maximumCyclomaticComplexity;
    }

    // ---------------------------------------------------------
    // Control-flow metrics
    // ---------------------------------------------------------

    public int getTotalLoops() {
        return totalLoops;
    }

    public void setTotalLoops(int totalLoops) {
        this.totalLoops = totalLoops;
    }

    public int getTotalConditionals() {
        return totalConditionals;
    }

    public void setTotalConditionals(int totalConditionals) {
        this.totalConditionals = totalConditionals;
    }

    public int getMaximumNestingDepth() {
        return maximumNestingDepth;
    }

    public void setMaximumNestingDepth(int maximumNestingDepth) {
        this.maximumNestingDepth = maximumNestingDepth;
    }

    // ---------------------------------------------------------
    // Coupling and inheritance
    // ---------------------------------------------------------

    public double getAverageFanIn() {
        return averageFanIn;
    }

    public void setAverageFanIn(double averageFanIn) {
        this.averageFanIn = averageFanIn;
    }

    public double getAverageFanOut() {
        return averageFanOut;
    }

    public void setAverageFanOut(double averageFanOut) {
        this.averageFanOut = averageFanOut;
    }

    public double getAverageCbo() {
        return averageCbo;
    }

    public void setAverageCbo(double averageCbo) {
        this.averageCbo = averageCbo;
    }

    public int getMaximumDit() {
        return maximumDit;
    }

    public void setMaximumDit(int maximumDit) {
        this.maximumDit = maximumDit;
    }

    public int getTotalNoc() {
        return totalNoc;
    }

    public void setTotalNoc(int totalNoc) {
        this.totalNoc = totalNoc;
    }

    // ---------------------------------------------------------
    // RFC
    // ---------------------------------------------------------

    public double getAverageRfc() {
        return averageRfc;
    }

    public void setAverageRfc(double averageRfc) {
        this.averageRfc = averageRfc;
    }
}