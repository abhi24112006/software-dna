package com.softwaredna.util;

import com.softwaredna.model.ParsedMethod;
import com.softwaredna.model.ParsedConstructor;
import com.softwaredna.model.ParsedParameter;

public final class IdentifierGenerator {

    private IdentifierGenerator() {

    }

    /*
     * ----------------------------------------------------
     * Core ID Builders
     * ----------------------------------------------------
     */

    private static String qualifiedName(
            String packageName,
            String simpleName) {

        if (packageName == null || packageName.isBlank()) {
            return simpleName;
        }

        return packageName + "." + simpleName;

    }

    private static String memberId(
            String ownerId,
            String memberName) {

        return ownerId + "#" + memberName;

    }

    /*
     * ----------------------------------------------------
     * Top-Level Entities
     * ----------------------------------------------------
     */

    public static String classId(
            String packageName,
            String className) {

        return qualifiedName(packageName, className);

    }

    public static String interfaceId(
            String packageName,
            String interfaceName) {

        return qualifiedName(packageName, interfaceName);

    }

    public static String enumId(
            String packageName,
            String enumName) {

        return qualifiedName(packageName, enumName);

    }

    public static String recordId(
            String packageName,
            String recordName) {

        return qualifiedName(packageName, recordName);

    }

    /*
     * ----------------------------------------------------
     * Members
     * ----------------------------------------------------
     */

    public static String fieldId(
            String ownerId,
            String fieldName) {

        return memberId(ownerId, fieldName);

    }

    public static String parameterId(
            String ownerId,
            String parameterName) {

        return memberId(ownerId, parameterName);

    }

    /*
     * ----------------------------------------------------
     * Executables
     * ----------------------------------------------------
     */

    public static String methodId(
            String ownerId,
            ParsedMethod method) {

        StringBuilder builder = new StringBuilder();

        builder.append(ownerId)
               .append("#")
               .append(method.getName())
               .append("(");

        for (int i = 0; i < method.getParameters().size(); i++) {

            ParsedParameter parameter =
                    method.getParameters().get(i);

            builder.append(parameter.getType());

            if (i < method.getParameters().size() - 1) {
                builder.append(",");
            }

        }

        builder.append(")");

        return builder.toString();

    }

    public static String constructorId(
            String ownerId,
            ParsedConstructor constructor) {

        StringBuilder builder = new StringBuilder();

        builder.append(ownerId)
               .append("#<init>(");

        for (int i = 0; i < constructor.getParameters().size(); i++) {

            ParsedParameter parameter =
                    constructor.getParameters().get(i);

            builder.append(parameter.getType());

            if (i < constructor.getParameters().size() - 1) {
                builder.append(",");
            }

        }

        builder.append(")");

        return builder.toString();

    }

}