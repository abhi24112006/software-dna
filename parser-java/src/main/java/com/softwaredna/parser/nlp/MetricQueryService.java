package com.softwaredna.analysis.metrics;

import java.util.Objects;

import com.softwaredna.model.ClassMetrics;
import com.softwaredna.model.ParsedClass;
import com.softwaredna.model.ParsedFile;
import com.softwaredna.model.RepositoryModel;

/**
 * Provides metric lookup operations over the normalized RepositoryModel.
 *
 * This service intentionally works on the language-independent model rather
 * than the KnowledgeGraph because metric data is stored on ParsedClass and
 * RepositoryModel.
 */
public class MetricQueryService {

    private final RepositoryModel repositoryModel;

    public MetricQueryService(RepositoryModel repositoryModel) {
        this.repositoryModel = Objects.requireNonNull(
                repositoryModel,
                "repositoryModel cannot be null"
        );
    }

    /**
     * Finds a class by name and returns its metrics.
     *
     * @param className class name to search for
     * @return ClassMetrics for the matching class
     * @throws IllegalArgumentException if the class cannot be found
     */
    public ClassMetrics getClassMetrics(String className) {
        if (className == null || className.isBlank()) {
            throw new IllegalArgumentException(
                    "className cannot be null or blank"
            );
        }

        for (ParsedFile file : repositoryModel.getFiles()) {
            if (file == null) {
                continue;
            }

            for (ParsedClass parsedClass : file.getClasses()) {
                if (parsedClass == null) {
                    continue;
                }

                if (className.equals(parsedClass.getName())) {
                    return parsedClass.getMetrics();
                }
            }
        }

        throw new IllegalArgumentException(
                "Class not found: " + className
        );
    }
}