package com.softwaredna.analysis;

import com.softwaredna.model.ParsedMethodCall;
import com.softwaredna.scope.Scope;

import java.util.ArrayList;
import java.util.List;

public class MethodAnalysisResult {

    /*
     * Local variables declared inside the method.
     */
    private final Scope scope = new Scope();

    /*
     * All method calls found inside the method body.
     */
    private final List<ParsedMethodCall> methodCalls =
            new ArrayList<>();

    public Scope getScope() {

        return scope;

    }

    public List<ParsedMethodCall> getMethodCalls() {

        return methodCalls;

    }

}