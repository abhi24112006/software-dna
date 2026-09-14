package com.softwaredna.analysis.metrics;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.softwaredna.analysis.metrics.java.JavaMethodMetricProvider;
import com.softwaredna.model.MethodMetrics;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedMethod;
import com.softwaredna.model.RepositoryModel;
import com.softwaredna.parser.language.JavaScriptParserAdapter;
import com.softwaredna.parser.language.PythonParserAdapter;

class CrossLanguageMetricIntegrationTest {

    @Test
    void javaProviderShouldPopulateNormalizedMethodMetrics()
            throws Exception {

        String source =
                """
                public class User {

                    public String process(
                            String value) {

                        String result =
                                value.toUpperCase();

                        System.out.println(result);

                        return result;
                    }
                }
                """;

        var compilationUnit =
                StaticJavaParser.parse(source);

        MethodDeclaration method =
                compilationUnit
                        .getClassByName("User")
                        .orElseThrow()
                        .getMethodsByName("process")
                        .get(0);

        MethodMetrics metrics =
                new JavaMethodMetricProvider()
                        .calculate(method);

        assertNotNull(metrics);

        assertEquals(
                1,
                metrics.getParameterCount()
        );

        assertEquals(
                1,
                metrics.getLocalVariableCount()
        );

        assertEquals(
                2,
                metrics.getMethodCallCount()
        );

        assertEquals(
                0,
                metrics.getObjectCreationCount()
        );

        assertEquals(
                1,
                metrics.getReturnCount()
        );

        assertEquals(
                1,
                metrics.getCyclomaticComplexity()
        );
    }

    @Test
    void pythonParserShouldPopulateNormalizedMethodMetrics()
            throws Exception {

        Path repository =
                Files.createTempDirectory(
                        "python-metrics"
                );

        Files.writeString(
                repository.resolve("user.py"),
                """
                class User:

                    def process(self, value):
                        result = value.upper()
                        print(result)
                        return result
                """
        );

        RepositoryModel model =
                new PythonParserAdapter().parse(
                        repository.toString()
                );

        assertNotNull(model);

        assertEquals(
                1,
                model.getFiles().size()
        );

        ParsedClass parsedClass =
                model.getFiles()
                        .get(0)
                        .getClasses()
                        .get(0);

        ParsedMethod method =
                findMethod(
                        parsedClass,
                        "process"
                );

        MethodMetrics metrics =
                method.getMetrics();

        assertNotNull(metrics);

        assertEquals(
                1,
                metrics.getParameterCount()
        );

        assertEquals(
                1,
                metrics.getLocalVariableCount()
        );

        assertEquals(
                2,
                metrics.getMethodCallCount()
        );

        assertEquals(
                0,
                metrics.getObjectCreationCount()
        );

        assertEquals(
                1,
                metrics.getReturnCount()
        );

        assertEquals(
                1,
                metrics.getCyclomaticComplexity()
        );
    }

    @Test
    void javaScriptParserShouldPopulateNormalizedMethodMetrics()
            throws Exception {

        Path repository =
                Files.createTempDirectory(
                        "js-metrics"
                );

        Files.writeString(
                repository.resolve("User.js"),
                """
                class User {

                    process(value) {
                        const result = value.toUpperCase();
                        console.log(result);
                        return result;
                    }

                }
                """
        );

        RepositoryModel model =
                new JavaScriptParserAdapter().parse(
                        repository.toString()
                );

        assertNotNull(model);

        assertEquals(
                1,
                model.getFiles().size()
        );

        ParsedClass parsedClass =
                model.getFiles()
                        .get(0)
                        .getClasses()
                        .get(0);

        ParsedMethod method =
                findMethod(
                        parsedClass,
                        "process"
                );

        MethodMetrics metrics =
                method.getMetrics();

        assertNotNull(metrics);

        assertEquals(
                1,
                metrics.getParameterCount()
        );

        assertEquals(
                1,
                metrics.getLocalVariableCount()
        );

        assertEquals(
                2,
                metrics.getMethodCallCount()
        );

        assertEquals(
                0,
                metrics.getObjectCreationCount()
        );

        assertEquals(
                1,
                metrics.getReturnCount()
        );

        assertEquals(
                1,
                metrics.getCyclomaticComplexity()
        );
    }

    private ParsedMethod findMethod(
            ParsedClass parsedClass,
            String methodName) {

        return parsedClass.getMethods()
                .stream()
                .filter(method ->
                        methodName.equals(
                                method.getName()
                        )
                )
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError(
                                "Method not found: "
                                        + methodName
                        )
                );
    }
}