package com.softwaredna.analysis.architecture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArchitectureSnapshotStore {

    private ArchitectureSnapshot previous;

    private ArchitectureSnapshot current;

    private final Path snapshotFile;

    private final Path historyFile;

    private final List<ArchitectureDiff> history;


    public ArchitectureSnapshotStore() {

        this.snapshotFile =
                Path.of(
                        "architecture-snapshot.dat"
                );

        this.historyFile =
                Path.of(
                        "architecture-history.dat"
                );

        this.history =
                new ArrayList<>();

        loadHistory();

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


    public List<ArchitectureDiff>
    getHistory() {

        return Collections.unmodifiableList(
                history
        );

    }


    public void addDiff(
            ArchitectureDiff diff) {

        if (diff == null) {

            return;

        }

        history.add(
                diff
        );

        saveHistory();

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


        ArchitectureDiff diff =
                comparator.compare(
                        previous,
                        current
                );


        addDiff(
                diff
        );


        return diff;

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


        for (String anomaly :
                report.getViolations()) {

            data.append(
                    anomaly
            );

            data.append(
                    "\n"
            );

        }


        return data.toString();

    }


    /*
     * ===================================================
     * Persistent Trend History
     * ===================================================
     */

    private void saveHistory() {

        try {

            StringBuilder data =
                    new StringBuilder();


            for (ArchitectureDiff diff :
                    history) {

                data.append(
                        escape(
                                diff.getPreviousStyle()
                        )
                );

                data.append("|");

                data.append(
                        escape(
                                diff.getCurrentStyle()
                        )
                );

                data.append("|");

                data.append(
                        diff.getPreviousHealth()
                );

                data.append("|");

                data.append(
                        diff.getCurrentHealth()
                );

                data.append("|");

                data.append(
                        encodeList(
                                diff.getAddedDependencies()
                        )
                );

                data.append("|");

                data.append(
                        encodeList(
                                diff.getRemovedDependencies()
                        )
                );

                data.append("|");

                data.append(
                        encodeList(
                                diff.getNewAnomalies()
                        )
                );

                data.append("|");

                data.append(
                        encodeList(
                                diff.getResolvedAnomalies()
                        )
                );

                data.append(
                        "\n"
                );

            }


            Files.writeString(
                    historyFile,
                    data.toString()
            );

        }
        catch (IOException e) {

            System.err.println(
                    "Warning: Could not save "
                            + "architecture history: "
                            + e.getMessage()
            );

        }

    }


    private void loadHistory() {

        if (!Files.exists(
                historyFile)) {

            return;

        }


        try {

            List<String> lines =
                    Files.readAllLines(
                            historyFile
                    );


            for (String line :
                    lines) {

                if (line.trim().isEmpty()) {

                    continue;

                }


                ArchitectureDiff diff =
                        parseDiff(
                                line
                        );


                if (diff != null) {

                    history.add(
                            diff
                    );

                }

            }

        }
        catch (IOException e) {

            System.err.println(
                    "Warning: Could not load "
                            + "architecture history: "
                            + e.getMessage()
            );

        }

    }


    private ArchitectureDiff parseDiff(
            String line) {

        try {

            String[] parts =
                    line.split(
                            "\\|",
                            -1
                    );


            if (parts.length != 8) {

                return null;

            }


            String previousStyle =
                    unescape(
                            parts[0]
                    );

            String currentStyle =
                    unescape(
                            parts[1]
                    );


            double previousHealth =
                    Double.parseDouble(
                            parts[2]
                    );

            double currentHealth =
                    Double.parseDouble(
                            parts[3]
                    );


            List<String> addedDependencies =
                    decodeList(
                            parts[4]
                    );

            List<String> removedDependencies =
                    decodeList(
                            parts[5]
                    );

            List<String> newAnomalies =
                    decodeList(
                            parts[6]
                    );

            List<String> resolvedAnomalies =
                    decodeList(
                            parts[7]
                    );


            return new ArchitectureDiff(
                    previousStyle,
                    currentStyle,
                    previousHealth,
                    currentHealth,
                    addedDependencies,
                    removedDependencies,
                    newAnomalies,
                    resolvedAnomalies
            );

        }
        catch (Exception e) {

            return null;

        }

    }


    private String encodeList(
            List<String> values) {

        if (values == null
                || values.isEmpty()) {

            return "";

        }


        StringBuilder result =
                new StringBuilder();


        for (int i = 0;
             i < values.size();
             i++) {

            if (i > 0) {

                result.append(
                        "~"
                );

            }


            result.append(
                    escape(
                            values.get(i)
                    )
            );

        }


        return result.toString();

    }


    private List<String> decodeList(
            String value) {

        List<String> result =
                new ArrayList<>();


        if (value == null
                || value.isEmpty()) {

            return result;

        }


        String[] values =
                value.split(
                        "~",
                        -1
                );


        for (String item :
                values) {

            result.add(
                    unescape(
                            item
                    )
            );

        }


        return result;

    }


    private String escape(
            String value) {

        if (value == null) {

            return "";

        }


        return value
                .replace(
                        "\\",
                        "\\\\"
                )
                .replace(
                        "|",
                        "\\p"
                )
                .replace(
                        "~",
                        "\\t"
                )
                .replace(
                        "\n",
                        "\\n"
                );

    }


    private String unescape(
            String value) {

        if (value == null) {

            return "";

        }


        return value
                .replace(
                        "\\n",
                        "\n"
                )
                .replace(
                        "\\t",
                        "~"
                )
                .replace(
                        "\\p",
                        "|"
                )
                .replace(
                        "\\\\",
                        "\\"
                );

    }

}