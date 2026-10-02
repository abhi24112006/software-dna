package com.softwaredna.relationship;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedField;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.ParsedMethod;
import com.softwaredna.model.ParsedParameter;
import com.softwaredna.model.RepositoryModel;

class UnresolvedReferenceTest {

    @Test
    void shouldNotCreateExtendsRelationshipForUnknownClass() {

        RepositoryModel repository =
                new RepositoryModel();

        ParsedFile file =
                new ParsedFile();

        file.setPackageName("com.example");

        ParsedClass child =
                new ParsedClass("Child");

        child.setId("com.example.Child");
        child.setSuperClass("UnknownParent");

        file.getClasses().add(child);
        repository.getFiles().add(file);

        new RelationshipExtractor()
                .extractRelationships(repository);

        assertTrue(
                repository.getRelationships().isEmpty()
        );
    }


    @Test
    void shouldNotCreateImplementsRelationshipForUnknownInterface() {

        RepositoryModel repository =
                new RepositoryModel();

        ParsedFile file =
                new ParsedFile();

        file.setPackageName("com.example");

        ParsedClass service =
                new ParsedClass("Service");

        service.setId("com.example.Service");

        service.getImplementedInterfaces()
                .add("UnknownInterface");

        file.getClasses().add(service);
        repository.getFiles().add(file);

        new RelationshipExtractor()
                .extractRelationships(repository);

        assertTrue(
                repository.getRelationships().isEmpty()
        );
    }


    @Test
    void shouldNotCreateFieldDependencyForUnknownType() {

        RepositoryModel repository =
                new RepositoryModel();

        ParsedFile file =
                new ParsedFile();

        file.setPackageName("com.example");

        ParsedClass service =
                new ParsedClass("Service");

        service.setId("com.example.Service");

        ParsedField field =
                new ParsedField(
                        "client",
                        "UnknownClient"
                );

        service.getFields().add(field);

        file.getClasses().add(service);
        repository.getFiles().add(file);

        new RelationshipExtractor()
                .extractRelationships(repository);

        assertTrue(
                repository.getRelationships().isEmpty()
        );
    }


    @Test
    void shouldNotCreateParameterDependencyForUnknownType() {

        RepositoryModel repository =
                new RepositoryModel();

        ParsedFile file =
                new ParsedFile();

        file.setPackageName("com.example");

        ParsedClass service =
                new ParsedClass("Service");

        service.setId("com.example.Service");

        ParsedMethod method =
                new ParsedMethod(
                        "process",
                        "com.example.Service#process()"
                );

        ParsedParameter parameter =
                new ParsedParameter(
                        "request",
                        "UnknownRequest"
                );

        method.getParameters().add(parameter);
        service.getMethods().add(method);

        file.getClasses().add(service);
        repository.getFiles().add(file);

        new RelationshipExtractor()
                .extractRelationships(repository);

        assertTrue(
                repository.getRelationships().isEmpty()
        );
    }


    @Test
    void shouldNotCreateReturnDependencyForUnknownType() {

        RepositoryModel repository =
                new RepositoryModel();

        ParsedFile file =
                new ParsedFile();

        file.setPackageName("com.example");

        ParsedClass service =
                new ParsedClass("Service");

        service.setId("com.example.Service");

        ParsedMethod method =
                new ParsedMethod(
                        "process",
                        "com.example.Service#process()"
                );

        method.setReturnType("UnknownResponse");

        service.getMethods().add(method);

        file.getClasses().add(service);
        repository.getFiles().add(file);

        new RelationshipExtractor()
                .extractRelationships(repository);

        assertTrue(
                repository.getRelationships().isEmpty()
        );
    }
}