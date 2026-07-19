package com.softwaredna.registry;

import com.softwaredna.model.*;

public class EntityRegistrar {

    public void registerEntities(
            RepositoryModel repository) {

        EntityRegistry registry =
                repository.getEntityRegistry();

        for (ParsedFile file : repository.getFiles()) {

            /*
             * Classes
             */

            for (ParsedClass parsedClass : file.getClasses()) {

                registry.registerClass(parsedClass);

                for (ParsedField field :
                        parsedClass.getFields()) {

                    registry.registerField(field);

                }

                for (ParsedConstructor constructor :
                        parsedClass.getConstructors()) {

                    registry.registerConstructor(constructor);

                }

                for (ParsedMethod method :
                        parsedClass.getMethods()) {

                    registry.registerMethod(method);

                    for (ParsedParameter parameter :
                            method.getParameters()) {

                        registry.registerParameter(parameter);

                    }

                }

            }

            /*
             * Interfaces
             */

            for (ParsedInterface parsedInterface :
                    file.getInterfaces()) {

                registry.registerInterface(parsedInterface);

            }

            /*
             * Enums
             */

            for (ParsedEnum parsedEnum :
                    file.getEnums()) {

                registry.registerEnum(parsedEnum);

            }

            /*
             * Records
             */

            for (ParsedRecord parsedRecord :
                    file.getRecords()) {

                registry.registerRecord(parsedRecord);

            }

        }

    }

}