package com.softwaredna.analysis.architecture;

import java.util.Collections;
import java.util.List;

public class ArchitectureSnapshot {

    private final ArchitectureReport report;

    private final ArchitectureHealthReport health;

    private final List<String> nodeIds;


    public ArchitectureSnapshot(
            ArchitectureReport report,
            ArchitectureHealthReport health,
            List<String> nodeIds) {

        this.report = report;
        this.health = health;

        this.nodeIds =
                Collections.unmodifiableList(
                        nodeIds
                );
    }


    public ArchitectureReport getReport() {

        return report;

    }


    public ArchitectureHealthReport getHealth() {

        return health;

    }


    public List<String> getNodeIds() {

        return nodeIds;

    }

}