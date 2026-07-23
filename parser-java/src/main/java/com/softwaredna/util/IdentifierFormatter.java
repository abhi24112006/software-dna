package com.softwaredna.util;

import com.softwaredna.model.EntityReference;
import com.softwaredna.model.EntityType;

public final class IdentifierFormatter {

    private IdentifierFormatter() {

    }

    public static String format(EntityReference reference) {

        if (reference == null) {
            return "null";
        }

        if (reference.getType() != EntityType.METHOD) {
            return reference.getName();
        }

        return formatMethodId(reference.getId());
    }

    private static String formatMethodId(String id) {

        if (id == null || id.isBlank()) {
            return "Unknown Method";
        }

        int lastDot = id.lastIndexOf('.');
        int hash = id.indexOf('#');

        if (hash == -1) {
            return id;
        }

        String className;

        if (lastDot == -1) {
            className = id.substring(0, hash);
        } else {
            className = id.substring(lastDot + 1, hash);
        }

        String method = id.substring(hash + 1);

        method = simplifyParameterTypes(method);

        return className + "." + method;
    }

    private static String simplifyParameterTypes(String methodSignature) {

        int open = methodSignature.indexOf('(');
        int close = methodSignature.lastIndexOf(')');

        if (open == -1 || close == -1) {
            return methodSignature;
        }

        String methodName = methodSignature.substring(0, open);
        String params = methodSignature.substring(open + 1, close);

        if (params.isBlank()) {
            return methodName + "()";
        }

        String[] split = params.split(",");

        StringBuilder builder = new StringBuilder();

        builder.append(methodName).append("(");

        for (int i = 0; i < split.length; i++) {

            String type = split[i].trim();

            int dot = type.lastIndexOf('.');

            if (dot != -1) {
                type = type.substring(dot + 1);
            }

            builder.append(type);

            if (i < split.length - 1) {
                builder.append(", ");
            }
        }

        builder.append(")");

        return builder.toString();
    }
}