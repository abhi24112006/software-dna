package com.softwaredna.extractor;

import com.softwaredna.model.ParsedAnnotation;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;

import java.util.ArrayList;
import java.util.List;

public class AnnotationExtractor {

    public List<ParsedAnnotation> extractAnnotations(
            NodeWithAnnotations<?> node) {

        List<ParsedAnnotation> annotations = new ArrayList<>();

        node.getAnnotations().forEach(annotation -> {
            annotations.add(
                    new ParsedAnnotation(
                            annotation.getNameAsString()
                    )
            );
        });

        return annotations;
    }

}