package com.softwaredna.model;

import java.util.ArrayList;
import java.util.List;

import com.softwaredna.analysis.MethodAnalysisResult;

public class ParsedMethod {

    private String name;

    private String id;

    private String returnType;

    private MethodMetrics metrics;

    /**
     * Source line where this method is declared.
     */
    private int sourceLineNumber;

    private List<ParsedParameter> parameters =
            new ArrayList<>();

    private List<ParsedAnnotation> annotations =
            new ArrayList<>();

    /*
     * Behavioral analysis for this method.
     */
    private MethodAnalysisResult analysisResult;


    public ParsedMethod() {
    }


    public ParsedMethod(
            String name,
            String returnType) {

        this.name = name;
        this.returnType = returnType;
    }


    public String getId() {
        return id;
    }


    public void setId(
            String id) {

        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(
            String name) {

        this.name = name;
    }


    public String getReturnType() {
        return returnType;
    }


    public void setReturnType(
            String returnType) {

        this.returnType = returnType;
    }


    public int getSourceLineNumber() {
        return sourceLineNumber;
    }


    public void setSourceLineNumber(
            int sourceLineNumber) {

        this.sourceLineNumber = sourceLineNumber;
    }


    public List<ParsedParameter> getParameters() {
        return parameters;
    }


    public void setParameters(
            List<ParsedParameter> parameters) {

        this.parameters = parameters;
    }


    public List<ParsedAnnotation> getAnnotations() {
        return annotations;
    }


    public void setAnnotations(
            List<ParsedAnnotation> annotations) {

        this.annotations = annotations;
    }


    public MethodAnalysisResult getAnalysisResult() {
        return analysisResult;
    }


    public void setAnalysisResult(
            MethodAnalysisResult analysisResult) {

        this.analysisResult = analysisResult;
    }


    public MethodMetrics getMetrics() {
        return metrics;
    }


    public void setMetrics(MethodMetrics metrics) {
        this.metrics = metrics;
    }
}