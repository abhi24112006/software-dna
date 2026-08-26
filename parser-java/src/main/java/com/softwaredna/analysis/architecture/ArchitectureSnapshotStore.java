package com.softwaredna.analysis.architecture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ArchitectureSnapshotStore {

    private ArchitectureSnapshot previous;

    private ArchitectureSnapshot current;

    private final Path snapshotFile;


    public ArchitectureSnapshotStore() {

        this.snapshotFile =
                Path.of(
                        "architecture-snapshot.dat"
                );

    }


    public void setPrevious(
            ArchitectureSnapshot snapshot) {

        this.previous =
                snapshot;

    }


    public void setCurrent(
            ArchitectureSnapshot snapshot) {

        this.current =
                snapshot;

    }


    public ArchitectureSnapshot getPrevious() {

        return previous;

    }


    public ArchitectureSnapshot getCurrent() {

        return current;

    }


    public boolean isReady() {

        return previous != null
                && current != null;

    }


    public void saveCurrentAsPrevious()
            throws IOException {

        if (current == null) {

            throw new IllegalStateException(
                    "No current architecture snapshot "
                            + "is available."
            );

        }


        previous =
                current;


        Files.writeString(
                snapshotFile,
                buildSnapshotData(
                        current
                )
        );

    }


    public String loadPreviousData()
            throws IOException {

        if (!Files.exists(
                snapshotFile)) {

            return null;

        }


        return Files.readString(
                snapshotFile
        );

    }


    private String buildSnapshotData(
            ArchitectureSnapshot snapshot) {

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


        for (String violation :
                report.getViolations()) {

            data.append(
                    violation
            );

            data.append(
                    "\n"
            );

        }


        return data.toString();

    }


    public ArchitectureSnapshot
    getPreviousSnapshot() {

        return previous;

    }


    public ArchitectureSnapshot
    getCurrentSnapshot() {

        return current;

    }


    public ArchitectureDiff compare() {

        if (!isReady()) {

            throw new IllegalStateException(
                    "Both previous and current "
                            + "architecture snapshots "
                            + "are required."
            );

        }


        ArchitectureSnapshotComparator
                comparator =
                new ArchitectureSnapshotComparator();


        return comparator.compare(
                previous,
                current
        );

    }

}