package com.softwaredna.resolver;

import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedMethod;
import com.softwaredna.model.ParsedMethodCall;
import com.softwaredna.registry.EntityRegistry;

import java.util.List;

public class MethodResolver {

    private final EntityResolver entityResolver =
            new EntityResolver();

    public ParsedMethod resolveMethod(
            String receiverType,
            ParsedMethodCall methodCall,
            EntityRegistry registry,
            ParsedClass currentClass) {

        if (receiverType == null ||
                methodCall == null ||
                registry == null) {

            return null;

        }

        List<ParsedClass> candidates =
                registry.findClassesByName(receiverType);

        if (candidates.isEmpty()) {

            return null;

        }

        String expectedPackage =
                currentClass == null ? null : currentClass.getPackageName();

        int argumentCount =
                methodCall.getArgumentExpressions().size();

        for (ParsedClass candidate : candidates) {

            if (expectedPackage != null && !expectedPackage.isBlank()) {

                if (candidate.getPackageName() != null
                        && !candidate.getPackageName().isBlank()
                        && !expectedPackage.equals(candidate.getPackageName())) {
                    continue;
                }

            }

            for (ParsedMethod method : candidate.getMethods()) {

                if (!method.getName().equals(
                        methodCall.getMethodName())) {
                    continue;
                }

                if (method.getParameters().size()
                        == argumentCount) {

                    return method;

                }

            }

        }

        for (ParsedClass candidate : candidates) {

            for (ParsedMethod method : candidate.getMethods()) {

                if (!method.getName().equals(
                        methodCall.getMethodName())) {
                    continue;
                }

                if (method.getParameters().size()
                        == argumentCount) {

                    return method;

                }

            }

        }

        return null;

    }

}