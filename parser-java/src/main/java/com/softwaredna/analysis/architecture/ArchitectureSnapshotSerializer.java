package com.softwaredna.analysis.architecture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ArchitectureSnapshotSerializer {

    private final Path file;


    public ArchitectureSnapshotSerializer(
            String filePath) {

        this.file =
                Path.of(filePath);

    }


    public void save(
            ArchitectureSnapshot snapshot)
            throws IOException {

        ArchitectureReport report =
                snapshot.getReport();

        ArchitectureHealthReport health =
                snapshot.getHealth();


        StringBuilder data =
                new StringBuilder();


        data.append(
                "STYLE="
        );

        data.append(
                report.getArchitectureStyle()
        );

        data.append(
                "\n"
        );


        data.append(
                "CONFIDENCE="
        );

        data.append(
                report.getConfidence()
        );

        data.append(
                "\n"
        );


        data.append(
                "HEALTH="
        );

        data.append(
                health.getOverallScore()
        );

        data.append(
                "\n"
        );


        data.append(
                "DEPENDENCIES\n"
        );


        for (ArchitectureEvidence evidence :
                report.getEvidence()) {

            data.append(
                    evidence.getSource()
            );

            data.append(
                    " --"
            );

            data.append(
                    evidence.getRelationship()
            );

            data.append(
                    "--> "
            );

            data.append(
                    evidence.getTarget()
            );

            data.append(
                    "\n"
            );

        }


        data.append(
                "ANOMALIES\n"
        );


        for (String anomaly :
                report.getViolations()) {

            data.append(
                    anomaly
            );

            data.append(
                    "\n"
            );

        }


        Files.writeString(
                file,
                data.toString()
        );

    }


    public String load()
            throws IOException {

        if (!Files.exists(file)) {

            return null;

        }


        return Files.readString(
                file
        );

    }


    public boolean exists() {

        return Files.exists(
                file
        );

    }

}