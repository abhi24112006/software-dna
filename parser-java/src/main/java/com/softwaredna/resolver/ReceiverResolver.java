package com.softwaredna.resolver;

import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedField;
import com.softwaredna.scope.Scope;

public class ReceiverResolver {

    public String resolveReceiverType(
            String receiverExpression,
            ParsedClass currentClass,
            Scope scope) {

        if (currentClass == null) {
            return null;
        }

        /*
         * Implicit receiver.
         *
         * helper();
         */
        if (receiverExpression == null ||
                receiverExpression.isBlank()) {

            return currentClass.getName();

        }

        /*
         * Explicit this.
         *
         * this.helper();
         */
        if ("this".equals(receiverExpression)) {

            return currentClass.getName();

        }

        /*
         * Explicit field access.
         *
         * this.teacher.teach();
         */
        if (receiverExpression.startsWith("this.")) {

            String fieldName = receiverExpression.substring(
                    "this.".length()
            );

            return resolveFieldType(currentClass, fieldName);

        }

        /*
         * Local variable.
         *
         * teacher.study();
         */
        if (scope != null) {

            String type =
                    scope.resolveVariable(receiverExpression);

            if (type != null) {

                return type;

            }

        }

        /*
         * Field receiver.
         *
         * teacher.study();
         */
        return resolveFieldType(currentClass, receiverExpression);

    }

    private String resolveFieldType(
            ParsedClass currentClass,
            String fieldName) {

        if (currentClass == null || fieldName == null || fieldName.isBlank()) {
            return null;
        }

        for (ParsedField field : currentClass.getFields()) {

            if (fieldName.equals(field.getName())) {

                return field.getType();

            }

        }

        return null;

    }

}