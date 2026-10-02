package com.softwaredna.relationship;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.softwaredna.identifier.IdentifierAssigner;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedField;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.ParsedInterface;
import com.softwaredna.model.Relationship;
import com.softwaredna.model.RelationshipType;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.registry.EntityRegistrar;

class CrossFileRelationshipTest {

    private void prepareRepository(
            RepositoryModel repository) {

        IdentifierAssigner identifierAssigner =
                new IdentifierAssigner();

        identifierAssigner.assignIds(repository);

        EntityRegistrar entityRegistrar =
                new EntityRegistrar();

        entityRegistrar.registerEntities(repository);
    }


    @Test
    void shouldResolveFieldDependencyAcrossFiles() {

        RepositoryModel repository =
                new RepositoryModel();

        ParsedFile repositoryFile =
                new ParsedFile();

        repositoryFile.setPackageName(
                "com.example.repository"
        );

        ParsedClass userRepository =
                new ParsedClass("UserRepository");

        repositoryFile.getClasses()
                .add(userRepository);


        ParsedFile serviceFile =
                new ParsedFile();

        serviceFile.setPackageName(
                "com.example.service"
        );

        serviceFile.getImports().add(
                "com.example.repository.UserRepository"
        );

        ParsedClass userService =
                new ParsedClass("UserService");

        ParsedField repositoryField =
                new ParsedField(
                        "repository",
                        "UserRepository"
                );

        userService.getFields()
                .add(repositoryField);

        serviceFile.getClasses()
                .add(userService);


        repository.getFiles().add(repositoryFile);
        repository.getFiles().add(serviceFile);

        prepareRepository(repository);

        new RelationshipExtractor()
                .extractRelationships(repository);


        List<Relationship> relationships =
                repository.getRelationships()
                        .stream()
                        .filter(r ->
                                r.getType()
                                        == RelationshipType.FIELD_DEPENDENCY
                        )
                        .toList();


        assertEquals(
                1,
                relationships.size()
        );

        Relationship relationship =
                relationships.get(0);

        assertEquals(
                "com.example.service.UserService",
                relationship.getSource().getId()
        );

        assertEquals(
                "com.example.repository.UserRepository",
                relationship.getTarget().getId()
        );
    }


    @Test
    void shouldResolveExtendsAcrossFiles() {

        RepositoryModel repository =
                new RepositoryModel();


        ParsedFile baseFile =
                new ParsedFile();

        baseFile.setPackageName(
                "com.example.base"
        );

        ParsedClass baseService =
                new ParsedClass("BaseService");

        baseFile.getClasses()
                .add(baseService);


        ParsedFile serviceFile =
                new ParsedFile();

        serviceFile.setPackageName(
                "com.example.service"
        );

        serviceFile.getImports().add(
                "com.example.base.BaseService"
        );

        ParsedClass userService =
                new ParsedClass("UserService");

        userService.setSuperClass(
                "BaseService"
        );

        serviceFile.getClasses()
                .add(userService);


        repository.getFiles().add(baseFile);
        repository.getFiles().add(serviceFile);

        prepareRepository(repository);

        new RelationshipExtractor()
                .extractRelationships(repository);


        List<Relationship> relationships =
                repository.getRelationships()
                        .stream()
                        .filter(r ->
                                r.getType()
                                        == RelationshipType.EXTENDS
                        )
                        .toList();


        assertEquals(
                1,
                relationships.size()
        );

        Relationship relationship =
                relationships.get(0);

        assertEquals(
                "com.example.service.UserService",
                relationship.getSource().getId()
        );

        assertEquals(
                "com.example.base.BaseService",
                relationship.getTarget().getId()
        );
    }


    @Test
    void shouldResolveImplementsAcrossFiles() {

        RepositoryModel repository =
                new RepositoryModel();


        ParsedFile interfaceFile =
                new ParsedFile();

        interfaceFile.setPackageName(
                "com.example.api"
        );

        ParsedInterface userService =
                new ParsedInterface("UserService");

        interfaceFile.getInterfaces()
                .add(userService);


        ParsedFile implementationFile =
                new ParsedFile();

        implementationFile.setPackageName(
                "com.example.service"
        );

        implementationFile.getImports().add(
                "com.example.api.UserService"
        );

        ParsedClass implementation =
                new ParsedClass("UserServiceImpl");

        implementation.getImplementedInterfaces()
                .add("UserService");

        implementationFile.getClasses()
                .add(implementation);


        repository.getFiles().add(interfaceFile);
        repository.getFiles().add(implementationFile);

        prepareRepository(repository);

        new RelationshipExtractor()
                .extractRelationships(repository);


        List<Relationship> relationships =
                repository.getRelationships()
                        .stream()
                        .filter(r ->
                                r.getType()
                                        == RelationshipType.IMPLEMENTS
                        )
                        .toList();


        assertEquals(
                1,
                relationships.size()
        );

        Relationship relationship =
                relationships.get(0);

        assertEquals(
                "com.example.service.UserServiceImpl",
                relationship.getSource().getId()
        );

        assertEquals(
                "com.example.api.UserService",
                relationship.getTarget().getId()
        );
    }


    @Test
    void shouldResolveFullyQualifiedTypeAcrossFiles() {

        RepositoryModel repository =
                new RepositoryModel();


        ParsedFile repositoryFile =
                new ParsedFile();

        repositoryFile.setPackageName(
                "com.example.repository"
        );

        ParsedClass userRepository =
                new ParsedClass("UserRepository");

        repositoryFile.getClasses()
                .add(userRepository);


        ParsedFile serviceFile =
                new ParsedFile();

        serviceFile.setPackageName(
                "com.example.service"
        );

        ParsedClass userService =
                new ParsedClass("UserService");

        ParsedField repositoryField =
                new ParsedField(
                        "repository",
                        "com.example.repository.UserRepository"
                );

        userService.getFields()
                .add(repositoryField);

        serviceFile.getClasses()
                .add(userService);


        repository.getFiles().add(repositoryFile);
        repository.getFiles().add(serviceFile);

        prepareRepository(repository);

        new RelationshipExtractor()
                .extractRelationships(repository);


        List<Relationship> relationships =
                repository.getRelationships()
                        .stream()
                        .filter(r ->
                                r.getType()
                                        == RelationshipType.FIELD_DEPENDENCY
                        )
                        .toList();


        assertEquals(
                1,
                relationships.size()
        );

        Relationship relationship =
                relationships.get(0);

        assertEquals(
                "com.example.repository.UserRepository",
                relationship.getTarget().getId()
        );

        assertTrue(
                relationship.getSource()
                        .getId()
                        .equals(
                                "com.example.service.UserService"
                        )
        );
    }
}