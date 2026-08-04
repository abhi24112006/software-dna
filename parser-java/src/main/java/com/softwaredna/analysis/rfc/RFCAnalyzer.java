package com.softwaredna.analysis.rfc;

import com.softwaredna.analysis.graph.GraphQueryService;
import com.softwaredna.analysis.graph.TypedDependency;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedMethod;

import java.util.HashSet;
import java.util.Set;

public class RFCAnalyzer {

    public RFCMetrics analyze(
            GraphQueryService query,
            ParsedClass parsedClass) {

        RFCMetrics metrics =
                new RFCMetrics();

        /*
         * Number of methods declared
         * in the class.
         */
        int declaredMethods =
                parsedClass.getMethods().size();

        /*
         * Collect distinct methods
         * called by all methods in
         * this class.
         */
        Set<String> calledMethods =
                new HashSet<>();

        for (ParsedMethod method :
                parsedClass.getMethods()) {

            for (TypedDependency dependency :
                    query.getCalledMethods(method.getId())) {

                calledMethods.add(
                        dependency.getTargetId());

            }

        }

        metrics.setRfc(
                declaredMethods
                        + calledMethods.size());

        return metrics;

    }

}