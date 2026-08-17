package com.softwaredna.identifier;

import com.softwaredna.model.*;
import com.softwaredna.util.IdentifierGenerator;

public class IdentifierAssigner {

    public void assignIds(
            RepositoryModel repository) {

        for (ParsedFile file : repository.getFiles()) {

            assignClassIds(file);
            assignInterfaceIds(file);

        }

    }

    /*
     * -------------------------------------------------------
     * Classes
     * -------------------------------------------------------
     */

    private void assignClassIds(
            ParsedFile file) {

        for (ParsedClass parsedClass : file.getClasses()) {

            String classId =
                    IdentifierGenerator.classId(
                            file.getPackageName(),
                            parsedClass.getName());

            parsedClass.setId(classId);

            assignFieldIds(parsedClass);

            assignConstructorIds(parsedClass);

            assignMethodIds(parsedClass);

        }

    }

    /*
     * -------------------------------------------------------
     * Interfaces
     * -------------------------------------------------------
     */

    private void assignInterfaceIds(
            ParsedFile file) {

        for (ParsedInterface parsedInterface :
                file.getInterfaces()) {

            String interfaceId =
                    IdentifierGenerator.interfaceId(
                            file.getPackageName(),
                            parsedInterface.getName());

            parsedInterface.setId(interfaceId);

        }

    }

    /*
     * -------------------------------------------------------
     * Fields
     * -------------------------------------------------------
     */

    private void assignFieldIds(
            ParsedClass parsedClass) {

        for (ParsedField field :
                parsedClass.getFields()) {

            field.setId(
                    IdentifierGenerator.fieldId(
                            parsedClass.getId(),
                            field.getName()));

        }

    }

    /*
     * -------------------------------------------------------
     * Constructors
     * -------------------------------------------------------
     */

    private void assignConstructorIds(
            ParsedClass parsedClass) {

        for (ParsedConstructor constructor :
                parsedClass.getConstructors()) {

            constructor.setId(
                    IdentifierGenerator.constructorId(
                            parsedClass.getId(),
                            constructor));

            assignParameterIds(
                    constructor.getId(),
                    constructor.getParameters());

        }

    }

    /*
     * -------------------------------------------------------
     * Methods
     * -------------------------------------------------------
     */

    private void assignMethodIds(
            ParsedClass parsedClass) {

        for (ParsedMethod method :
                parsedClass.getMethods()) {

            method.setId(
                    IdentifierGenerator.methodId(
                            parsedClass.getId(),
                            method));

            assignParameterIds(
                    method.getId(),
                    method.getParameters());

        }

    }

    /*
     * -------------------------------------------------------
     * Parameters
     * -------------------------------------------------------
     */

    private void assignParameterIds(
            String ownerId,
            java.util.List<ParsedParameter> parameters) {

        for (ParsedParameter parameter :
                parameters) {

            parameter.setId(
                    IdentifierGenerator.parameterId(
                            ownerId,
                            parameter.getName()));

        }

    }

}