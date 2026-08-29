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

public class PythonParserAdapter
        implements LanguageParser {

    private final ObjectMapper objectMapper;

    private final IdentifierAssigner identifierAssigner;

    private final EntityRegistrar registrar;

    private final List<PythonCall> pythonCalls;

    private final PythonRelationshipExtractor
            relationshipExtractor;

    public PythonParserAdapter() {

        objectMapper =
                new ObjectMapper();

        identifierAssigner =
                new IdentifierAssigner();

        registrar =
                new EntityRegistrar();

        pythonCalls =
                new ArrayList<>();

        relationshipExtractor =
                new PythonRelationshipExtractor();
    }

    @Override
    public Language getLanguage() {

        return Language.PYTHON;
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

        pythonCalls.clear();

        String json =
                runPythonParser(
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

            ParsedFile parsedFile =
                    convertFile(fileData);

            repository.getFiles().add(
                    parsedFile
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
         * Extract Python relationships.
         */
        relationshipExtractor.extractRelationships(
                repository,
                pythonCalls
        );

        return repository;
    }

    private String runPythonParser(
            String repositoryPath)
            throws IOException {

        Path script =
                Path.of(
                        "src",
                        "resources",
                        "python",
                        "python_ast_parser.py"
                );

        if (!Files.exists(script)) {

            throw new IOException(
                    "Python AST parser not found: "
                            + script.toAbsolutePath()
            );
        }

        ProcessBuilder processBuilder =
                new ProcessBuilder(
                        "python",
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
                        "Python AST parser failed "
                                + "with exit code "
                                + exitCode
                                + "\n"
                                + output
                );
            }

        }
        catch (InterruptedException e) {

            Thread.currentThread()
                    .interrupt();

            throw new IOException(
                    "Python AST parser was interrupted.",
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

        if (
                superClasses != null
                        && !superClasses.isEmpty()
        ) {

            parsedClass.setSuperClass(
                    superClasses.get(0)
            );
        }

        /*
         * Fields
         */
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
                        (String)
                                fieldData.get(
                                        "name"
                                );

                String fieldType =
                        (String)
                                fieldData.get(
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

        /*
         * Methods
         */
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
                        classData,
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

                String parameterName =
                        (String)
                                parameterData.get(
                                        "name"
                                );

                String parameterType =
                        (String)
                                parameterData.get(
                                        "type"
                                );

                ParsedParameter parameter =
                        new ParsedParameter();

                parameter.setName(
                        parameterName
                );

                parameter.setType(
                        parameterType
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
            Map<String, Object> classData,
            Map<String, Object> methodData,
            ParsedMethod method) {

        String sourceClass =
                (String) classData.get(
                        "name"
                );

        String sourceMethod =
                method.getName();

        List<Map<String, Object>> calls =
                (List<Map<String, Object>>)
                        methodData.get(
                                "calls"
                        );

        if (calls == null) {
            return;
        }

        for (
                Map<String, Object> call :
                calls
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

            pythonCalls.add(
                    new PythonCall(
                            sourceClass,
                            sourceMethod,
                            receiver,
                            targetMethod
                    )
            );
        }
    }

    public List<PythonCall> getPythonCalls() {

        return new ArrayList<>(
                pythonCalls
        );
    }

    private String derivePackageName(
            String filePath) {

        if (
                filePath == null
                        || filePath.isBlank()
        ) {

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