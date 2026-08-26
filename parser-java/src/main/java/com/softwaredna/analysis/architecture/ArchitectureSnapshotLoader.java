package com.softwaredna.analysis.architecture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ArchitectureSnapshotLoader {

    private final Path snapshotFile;


    public ArchitectureSnapshotLoader() {

        this.snapshotFile =
                Path.of(
                        "architecture-snapshot.dat"
                );

    }


    public boolean exists() {

        return Files.exists(
                snapshotFile
        );

    }


    public SnapshotData load()
            throws IOException {

        if (!exists()) {

            return null;

        }


        List<String> lines =
                Files.readAllLines(
                        snapshotFile
                );


        String style = "";

        double confidence = 0.0;

        double health = 0.0;

        List<String> dependencies =
                new ArrayList<>();

        List<String> anomalies =
                new ArrayList<>();


        String section = "";


        for (String line : lines) {

            if (line.startsWith(
                    "STYLE=")) {

                style =
                        line.substring(
                                "STYLE=".length()
                        );

            }
            else if (line.startsWith(
                    "CONFIDENCE=")) {

                confidence =
                        Double.parseDouble(
                                line.substring(
                                        "CONFIDENCE="
                                                .length()
                                )
                        );

            }
            else if (line.startsWith(
                    "HEALTH=")) {

                health =
                        Double.parseDouble(
                                line.substring(
                                        "HEALTH="
                                                .length()
                                )
                        );

            }
            else if (line.equals(
                    "DEPENDENCIES")) {

                section =
                        "DEPENDENCIES";

            }
            else if (line.equals(
                    "ANOMALIES")) {

                section =
                        "ANOMALIES";

            }
            else if (!line.isBlank()) {

                if ("DEPENDENCIES".equals(
                        section)) {

                    dependencies.add(
                            line
                    );

                }
                else if ("ANOMALIES".equals(
                        section)) {

                    anomalies.add(
                            line
                    );

                }

            }

        }


        return new SnapshotData(
                style,
                confidence,
                health,
                dependencies,
                anomalies
        );

    }


    public static class SnapshotData {

        private final String style;

        private final double confidence;

        private final double health;

        private final List<String>
                dependencies;

        private final List<String>
                anomalies;


        public SnapshotData(
                String style,
                double confidence,
                double health,
                List<String> dependencies,
                List<String> anomalies) {

            this.style =
                    style;

            this.confidence =
                    confidence;

            this.health =
                    health;

            this.dependencies =
                    dependencies;

            this.anomalies =
                    anomalies;

        }


        public String getStyle() {

            return style;

        }


        public double getConfidence() {

            return confidence;

        }


        public double getHealth() {

            return health;

        }


        public List<String>
        getDependencies() {

            return dependencies;

        }


        public List<String>
        getAnomalies() {

            return anomalies;

        }

    }

}