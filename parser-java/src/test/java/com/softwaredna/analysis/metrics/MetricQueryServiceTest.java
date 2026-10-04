package com.softwaredna.analysis.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.softwaredna.model.ClassMetrics;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.RepositoryModel;

class MetricQueryServiceTest {

    @Test
    void shouldReturnMetricsForExistingClass() {
        RepositoryModel repositoryModel = new RepositoryModel();

        ParsedFile file = new ParsedFile();
        ParsedClass parsedClass = new ParsedClass("UserService");

        ClassMetrics metrics = new ClassMetrics();
        metrics.setMethodCount(5);
        metrics.setFieldCount(3);
        metrics.setTotalLinesOfCode(120);

        parsedClass.setMetrics(metrics);
        file.getClasses().add(parsedClass);
        repositoryModel.getFiles().add(file);

        MetricQueryService service =
                new MetricQueryService(repositoryModel);

        ClassMetrics result =
                service.getClassMetrics("UserService");

        assertNotNull(result);
        assertEquals(5, result.getMethodCount());
        assertEquals(3, result.getFieldCount());
        assertEquals(120, result.getTotalLinesOfCode());
    }

    @Test
    void shouldThrowExceptionForUnknownClass() {
        RepositoryModel repositoryModel = new RepositoryModel();

        MetricQueryService service =
                new MetricQueryService(repositoryModel);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.getClassMetrics("UnknownClass")
                );

        assertEquals(
                "Class not found: UnknownClass",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankClassName() {
        RepositoryModel repositoryModel = new RepositoryModel();

        MetricQueryService service =
                new MetricQueryService(repositoryModel);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getClassMetrics(" ")
        );
    }

    @Test
    void shouldRejectNullRepositoryModel() {
        assertThrows(
                NullPointerException.class,
                () -> new MetricQueryService(null)
        );
    }
}