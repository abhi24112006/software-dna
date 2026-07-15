package com.softwaredna.extractor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.softwaredna.model.ParsedRecord;

import java.util.ArrayList;
import java.util.List;

public class RecordExtractor {

    public List<ParsedRecord> extractRecords(CompilationUnit cu) {

        List<ParsedRecord> records = new ArrayList<>();

        for (RecordDeclaration declaration :
                cu.findAll(RecordDeclaration.class)) {

            ParsedRecord parsedRecord =
                    new ParsedRecord(
                            declaration.getNameAsString()
                    );

            records.add(parsedRecord);

        }

        return records;

    }

}