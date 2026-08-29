package com.softwaredna.parser.language;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwaredna.identifier.IdentifierAssigner;
import com.softwaredna.language.Language;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedField;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.ParsedMethod;
import com.softwaredna.model.ParsedParameter;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.registry.EntityRegistrar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaScriptParserAdapter
        implements LanguageParser {

    private final ObjectMapper objectMapper;

    private final IdentifierAssigner identifierAssigner;

    private final EntityRegistrar registrar;

    private final List<JavaScriptCall> calls;

    private final JavaScriptRelationshipExtractor
            relationshipExtractor;

    public JavaScriptParserAdapter() {

        objectMapper =
                new ObjectMapper();

        identifierAssigner =
                new IdentifierAssigner();

        registrar =
                new EntityRegistrar();

        calls =
                new ArrayList<>();

        relationshipExtractor =
                new JavaScriptRelationshipExtractor();
    }

    @Override
    public Language getLanguage() {

        return Language.JAVASCRIPT;
    }

    @Override
    public RepositoryModel parse(
            String repositoryPath)
            throws IOException {

        RepositoryModel repository =
                new RepositoryModel();

        Path root =
                Path.of(repositoryPath);

        repository.setRepositoryName(
                root.getFileName().toString()
        );

        calls.clear();

        String json =
                runJavaScriptParser(
                        repositoryPath
                );

        List<Map<String, Object>> files =
                objectMapper.readValue(
                        json,
                        new TypeReference<
                                List<Map<String, Object>>
                                >() {}
                );

        for (Map<String, Object> fileData :
                files) {

            ParsedFile file =
                    convertFile(fileData);

            repository.getFiles().add(
                    file
            );
        }

        /*
         * Assign IDs.
         */
        identifierAssigner.assignIds(
                repository
        );

        /*
         * Register entities.
         */
        registrar.registerEntities(
                repository
        );

        /*
         * Extract JavaScript relationships.
         */
        relationshipExtractor.extractRelationships(
                repository,
                calls
        );

        return repository;
    }

    private String runJavaScriptParser(
            String repositoryPath)
            throws IOException {

        Path script =
                Path.of(
                        "src",
                        "resources",
                        "javascript",
                        "javascript_ast_parser.js"
                );

        if (!Files.exists(script)) {

            throw new IOException(
                    "JavaScript AST parser not found: "
                            + script.toAbsolutePath()
            );
        }

        ProcessBuilder processBuilder =
                new ProcessBuilder(
                        "node",
                        script.toString(),
                        repositoryPath
                );

        processBuilder.redirectErrorStream(
                true
        );

        Process process =
                processBuilder.start();

        StringBuilder output =
                new StringBuilder();

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        process.getInputStream()
                                )
                        )
        ) {

            String line;

            while (
                    (line = reader.readLine())
                            != null
            ) {

                output.append(line)
                        .append(
                                System.lineSeparator()
                        );
            }
        }

        try {

            int exitCode =
                    process.waitFor();

            if (exitCode != 0) {

                throw new IOException(
                        "JavaScript AST parser failed "
                                + "with exit code "
                                + exitCode
                                + "\n"
                                + output
                );
            }

        } catch (InterruptedException e) {

            Thread.currentThread()
                    .interrupt();

            throw new IOException(
                    "JavaScript AST parser was interrupted.",
                    e
            );
        }

        return output.toString();
    }

    @SuppressWarnings("unchecked")
    private ParsedFile convertFile(
            Map<String, Object> fileData) {

        ParsedFile file =
                new ParsedFile();

        String filePath =
                (String) fileData.get(
                        "file"
                );

        file.setPackageName(
                derivePackageName(
                        filePath
                )
        );

        List<String> imports =
                (List<String>)
                        fileData.get(
                                "imports"
                        );

        if (imports != null) {

            file.getImports()
                    .addAll(imports);
        }

        List<Map<String, Object>> classes =
                (List<Map<String, Object>>)
                        fileData.get(
                                "classes"
                        );

        if (classes != null) {

            for (
                    Map<String, Object> classData :
                    classes
            ) {

                ParsedClass parsedClass =
                        convertClass(
                                classData,
                                file.getPackageName()
                        );

                file.getClasses().add(
                        parsedClass
                );
            }
        }

        return file;
    }

    @SuppressWarnings("unchecked")
    private ParsedClass convertClass(
            Map<String, Object> classData,
            String packageName) {

        String name =
                (String) classData.get(
                        "name"
                );

        ParsedClass parsedClass =
                new ParsedClass(name);

        parsedClass.setPackageName(
                packageName
        );

        List<String> superClasses =
                (List<String>)
                        classData.get(
                                "superClasses"
                        );

        if (superClasses != null &&
                !superClasses.isEmpty()) {

            parsedClass.setSuperClass(
                    superClasses.get(0)
            );
        }

        List<Map<String, Object>> fields =
                (List<Map<String, Object>>)
                        classData.get(
                                "fields"
                        );

        if (fields != null) {

            for (
                    Map<String, Object> fieldData :
                    fields
            ) {

                String fieldName =
                        (String) fieldData.get(
                                "name"
                        );

                String fieldType =
                        (String) fieldData.get(
                                "type"
                        );

                parsedClass.getFields().add(
                        new ParsedField(
                                fieldName,
                                fieldType
                        )
                );
            }
        }

        List<Map<String, Object>> methods =
                (List<Map<String, Object>>)
                        classData.get(
                                "methods"
                        );

        if (methods != null) {

            for (
                    Map<String, Object> methodData :
                    methods
            ) {

                ParsedMethod method =
                        convertMethod(
                                methodData
                        );

                parsedClass.getMethods().add(
                        method
                );

                extractCalls(
                        parsedClass,
                        methodData,
                        method
                );
            }
        }

        return parsedClass;
    }

    @SuppressWarnings("unchecked")
    private ParsedMethod convertMethod(
            Map<String, Object> methodData) {

        String name =
                (String) methodData.get(
                        "name"
                );

        String returnType =
                (String) methodData.get(
                        "returnType"
                );

        ParsedMethod method =
                new ParsedMethod(
                        name,
                        returnType
                );

        List<Map<String, Object>> parameters =
                (List<Map<String, Object>>)
                        methodData.get(
                                "parameters"
                        );

        if (parameters != null) {

            for (
                    Map<String, Object> parameterData :
                    parameters
            ) {

                ParsedParameter parameter =
                        new ParsedParameter();

                parameter.setName(
                        (String)
                                parameterData.get(
                                        "name"
                                )
                );

                parameter.setType(
                        (String)
                                parameterData.get(
                                        "type"
                                )
                );

                method.getParameters().add(
                        parameter
                );
            }
        }

        return method;
    }

    @SuppressWarnings("unchecked")
    private void extractCalls(
            ParsedClass parsedClass,
            Map<String, Object> methodData,
            ParsedMethod method) {

        List<Map<String, Object>> methodCalls =
                (List<Map<String, Object>>)
                        methodData.get(
                                "calls"
                        );

        if (methodCalls == null) {
            return;
        }

        for (
                Map<String, Object> call :
                methodCalls
        ) {

            String receiver =
                    (String) call.get(
                            "receiver"
                    );

            String targetMethod =
                    (String) call.get(
                            "method"
                    );

            if (targetMethod == null) {
                continue;
            }

            calls.add(
                    new JavaScriptCall(
                            parsedClass.getName(),
                            method.getName(),
                            receiver,
                            targetMethod
                    )
            );
        }
    }

    public List<JavaScriptCall> getCalls() {

        return new ArrayList<>(
                calls
        );
    }

    private String derivePackageName(
            String filePath) {

        if (filePath == null ||
                filePath.isBlank()) {

            return "";
        }

        Path path =
                Path.of(filePath);

        Path parent =
                path.getParent();

        if (parent == null) {
            return "";
        }

        return parent.toString()
                .replace('\\', '.')
                .replace('/', '.');
    }
}