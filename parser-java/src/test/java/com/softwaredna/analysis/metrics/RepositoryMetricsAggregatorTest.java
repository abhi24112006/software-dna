package com.softwaredna.analysis.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.softwaredna.model.ClassMetrics;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedEnum;
import com.softwaredna.model.ParsedField;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.ParsedInterface;
import com.softwaredna.model.ParsedMethod;
import com.softwaredna.model.ParsedRecord;
import com.softwaredna.model.RepositoryMetrics;
import com.softwaredna.model.RepositoryModel;

class RepositoryMetricsAggregatorTest {

    @Test
    void shouldAggregateRepositoryStructureMetrics() {

        RepositoryModel repository = createRepository();

        RepositoryMetrics metrics =
                new RepositoryMetricsAggregator()
                        .aggregate(repository);

        assertEquals(2, metrics.getTotalFiles());
        assertEquals(2, metrics.getTotalClasses());
        assertEquals(1, metrics.getTotalInterfaces());
        assertEquals(1, metrics.getTotalEnums());
        assertEquals(1, metrics.getTotalRecords());

        assertEquals(3, metrics.getTotalMethods());
        assertEquals(3, metrics.getTotalFields());
        assertEquals(2, metrics.getTotalConstructors());
    }

    @Test
    void shouldAggregateMethodMetrics() {

        RepositoryModel repository = createRepository();

        RepositoryMetrics metrics =
                new RepositoryMetricsAggregator()
                        .aggregate(repository);

        assertEquals(30, metrics.getTotalLinesOfCode());
        assertEquals(5, metrics.getTotalParameters());
        assertEquals(7, metrics.getTotalLocalVariables());
        assertEquals(9, metrics.getTotalMethodCalls());
        assertEquals(4, metrics.getTotalObjectCreations());
        assertEquals(3, metrics.getTotalReturnStatements());
    }

    @Test
    void shouldAggregateComplexityMetrics() {

        RepositoryModel repository = createRepository();

        RepositoryMetrics metrics =
                new RepositoryMetricsAggregator()
                        .aggregate(repository);

        assertEquals(
                7,
                metrics.getTotalCyclomaticComplexity()
        );

        assertEquals(
                3,
                metrics.getMaximumCyclomaticComplexity()
        );

        assertEquals(5, metrics.getTotalLoops());
        assertEquals(4, metrics.getTotalConditionals());
        assertEquals(3, metrics.getMaximumNestingDepth());

        assertEquals(
                3.5,
                metrics.getAverageCyclomaticComplexity(),
                0.0001
        );
    }

    @Test
    void shouldAggregateCouplingInheritanceAndRfcMetrics() {

        RepositoryModel repository = createRepository();

        RepositoryMetrics metrics =
                new RepositoryMetricsAggregator()
                        .aggregate(repository);

        assertEquals(
                1.5,
                metrics.getAverageFanIn(),
                0.0001
        );

        assertEquals(
                2.5,
                metrics.getAverageFanOut(),
                0.0001
        );

        assertEquals(
                2.0,
                metrics.getAverageCbo(),
                0.0001
        );

        assertEquals(3, metrics.getMaximumDit());

        assertEquals(4, metrics.getTotalNoc());

        assertEquals(
                3.5,
                metrics.getAverageRfc(),
                0.0001
        );
    }

    @Test
    void shouldCalculateAverageMethodLinesOfCodeUsingMethodsWithMetrics() {

        RepositoryModel repository = createRepository();

        RepositoryMetrics metrics =
                new RepositoryMetricsAggregator()
                        .aggregate(repository);

        assertEquals(
                10.0,
                metrics.getAverageMethodLinesOfCode(),
                0.0001
        );
    }

    @Test
    void shouldIgnoreMethodsWithoutMetricsWhenCalculatingAverage() {

        RepositoryModel repository = new RepositoryModel();

        ParsedFile file = new ParsedFile();

        ParsedClass parsedClass =
                new ParsedClass("TestClass");

        ParsedMethod method =
                new ParsedMethod();

        method.setMetrics(
                createMethodMetrics(
                        10,
                        2,
                        1,
                        1,
                        1,
                        1,
                        2,
                        1,
                        1,
                        1
                )
        );

        ParsedMethod methodWithoutMetrics =
                new ParsedMethod();

        parsedClass.getMethods().add(method);
        parsedClass.getMethods().add(methodWithoutMetrics);

        ClassMetrics classMetrics =
                new ClassMetrics();

        classMetrics.setMethodCount(2);
        classMetrics.setTotalLinesOfCode(10);

        parsedClass.setMetrics(classMetrics);

        file.getClasses().add(parsedClass);
        repository.getFiles().add(file);

        RepositoryMetrics metrics =
                new RepositoryMetricsAggregator()
                        .aggregate(repository);

        /*
         * Only the method with actual MethodMetrics should
         * contribute to the repository average.
         */
        assertEquals(
                10.0,
                metrics.getAverageMethodLinesOfCode(),
                0.0001
        );
    }

    @Test
    void shouldReturnEmptyMetricsForNullRepository() {

        RepositoryMetrics metrics =
                new RepositoryMetricsAggregator()
                        .aggregate(null);

        assertEquals(0, metrics.getTotalFiles());
        assertEquals(0, metrics.getTotalClasses());
        assertEquals(0, metrics.getTotalMethods());
        assertEquals(0, metrics.getTotalLinesOfCode());

        assertEquals(
                0.0,
                metrics.getAverageMethodLinesOfCode(),
                0.0001
        );

        assertEquals(
                0.0,
                metrics.getAverageCyclomaticComplexity(),
                0.0001
        );
    }

    private RepositoryModel createRepository() {

        RepositoryModel repository =
                new RepositoryModel();

        ParsedFile file1 =
                new ParsedFile();

        ParsedFile file2 =
                new ParsedFile();

        /*
         * Class 1
         *
         * 1 method
         * 2 fields
         * 1 constructor
         */
        ParsedClass class1 =
                createClass(
                        "ClassOne",
                        10,
                        2,
                        1,
                        3,
                        2,
                        1,
                        2,
                        2,
                        1,
                        1,
                        1,
                        2,
                        1,
                        3,
                        2,
                        1
                );

        /*
         * Class 2
         *
         * 2 methods
         * 1 field
         * 1 constructor
         */
        ParsedClass class2 =
                createClass(
                        "ClassTwo",
                        20,
                        3,
                        6,
                        6,
                        2,
                        2,
                        5,
                        3,
                        3,
                        2,
                        2,
                        1,
                        3,
                        2,
                        4,
                        2
                );

        class2.getFields().remove(1);

        /*
         * Add one extra method to ClassTwo.
         */
        ParsedMethod secondMethod =
                new ParsedMethod();

        secondMethod.setMetrics(
                createMethodMetrics(
                        10,
                        1,
                        3,
                        3,
                        0,
                        1,
                        2,
                        1,
                        1,
                        2
                )
        );

        class2.getMethods().add(secondMethod);

        /*
         * Class-level totals for ClassTwo must represent
         * both methods.
         */
        ClassMetrics class2Metrics =
                class2.getMetrics();

        class2Metrics.setMethodCount(2);
        class2Metrics.setTotalLinesOfCode(20);
        class2Metrics.setTotalParameters(3);
        class2Metrics.setTotalLocalVariables(6);
        class2Metrics.setTotalMethodCalls(6);
        class2Metrics.setTotalObjectCreations(2);
        class2Metrics.setTotalReturnStatements(2);

        class2Metrics.setTotalCyclomaticComplexity(5);
        class2Metrics.setMaximumCyclomaticComplexity(3);

        class2Metrics.setTotalLoopCount(3);
        class2Metrics.setMaximumLoopCount(2);

        class2Metrics.setTotalConditionalCount(3);
        class2Metrics.setMaximumConditionalCount(2);

        class2Metrics.setMaximumNestingDepth(3);

        class2Metrics.setFanIn(2);
        class2Metrics.setFanOut(3);
        class2Metrics.setCbo(3);
        class2Metrics.setDit(3);
        class2Metrics.setNoc(3);
        class2Metrics.setRfc(5);

        file1.getClasses().add(class1);
        file2.getClasses().add(class2);

        /*
         * Additional repository-level entities.
         */
        file1.getInterfaces().add(
                new ParsedInterface()
        );

        file1.getEnums().add(
                new ParsedEnum()
        );

        file2.getRecords().add(
                new ParsedRecord()
        );

        repository.getFiles().add(file1);
        repository.getFiles().add(file2);

        return repository;
    }

    private ParsedClass createClass(
            String name,
            int linesOfCode,
            int parameters,
            int localVariables,
            int methodCalls,
            int objectCreations,
            int returnStatements,
            int cyclomaticComplexity,
            int loops,
            int conditionals,
            int nestingDepth,
            int fanIn,
            int fanOut,
            int cbo,
            int dit,
            int rfc,
            int noc) {

        ParsedClass parsedClass =
                new ParsedClass(name);

        /*
         * Fields
         */
        parsedClass.getFields().add(
                new ParsedField()
        );

        parsedClass.getFields().add(
                new ParsedField()
        );

        /*
         * Constructor
         */
        parsedClass.getConstructors().add(
                new com.softwaredna.model.ParsedConstructor()
        );

        /*
         * One real method.
         */
        ParsedMethod method =
                new ParsedMethod();

        method.setMetrics(
                createMethodMetrics(
                        linesOfCode,
                        parameters,
                        localVariables,
                        methodCalls,
                        objectCreations,
                        returnStatements,
                        cyclomaticComplexity,
                        loops,
                        conditionals,
                        nestingDepth
                )
        );

        parsedClass.getMethods().add(method);

        /*
         * Class-level metrics.
         */
        ClassMetrics classMetrics =
                new ClassMetrics();

        classMetrics.setFieldCount(
                parsedClass.getFields().size()
        );

        classMetrics.setConstructorCount(
                parsedClass.getConstructors().size()
        );

        classMetrics.setMethodCount(1);

        classMetrics.setTotalLinesOfCode(linesOfCode);
        classMetrics.setTotalParameters(parameters);
        classMetrics.setTotalLocalVariables(localVariables);
        classMetrics.setTotalMethodCalls(methodCalls);
        classMetrics.setTotalObjectCreations(objectCreations);
        classMetrics.setTotalReturnStatements(returnStatements);

        classMetrics.setTotalCyclomaticComplexity(
                cyclomaticComplexity
        );

        classMetrics.setMaximumCyclomaticComplexity(
                cyclomaticComplexity
        );

        classMetrics.setTotalLoopCount(loops);
        classMetrics.setMaximumLoopCount(loops);

        classMetrics.setTotalConditionalCount(conditionals);
        classMetrics.setMaximumConditionalCount(conditionals);

        classMetrics.setMaximumNestingDepth(nestingDepth);

        classMetrics.setFanIn(fanIn);
        classMetrics.setFanOut(fanOut);
        classMetrics.setCbo(cbo);
        classMetrics.setDit(dit);
        classMetrics.setNoc(noc);
        classMetrics.setRfc(rfc);

        parsedClass.setMetrics(classMetrics);

        return parsedClass;
    }

    private com.softwaredna.model.MethodMetrics createMethodMetrics(
            int linesOfCode,
            int parameters,
            int localVariables,
            int methodCalls,
            int objectCreations,
            int returnStatements,
            int cyclomaticComplexity,
            int loops,
            int conditionals,
            int nestingDepth) {

        com.softwaredna.model.MethodMetrics metrics =
                new com.softwaredna.model.MethodMetrics();

        metrics.setLinesOfCode(linesOfCode);
        metrics.setParameterCount(parameters);
        metrics.setLocalVariableCount(localVariables);
        metrics.setMethodCallCount(methodCalls);
        metrics.setObjectCreationCount(objectCreations);
        metrics.setReturnCount(returnStatements);
        metrics.setCyclomaticComplexity(cyclomaticComplexity);
        metrics.setLoopCount(loops);
        metrics.setConditionalCount(conditionals);
        metrics.setMaximumNestingDepth(nestingDepth);

        return metrics;
    }
}