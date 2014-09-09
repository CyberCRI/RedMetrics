package org.cri.redmetrics.backend;

import org.cri.redmetrics.model.TestSnapshot;

public class SnapshotBackend extends ProgressDataBackend<TestSnapshot> {

    SnapshotBackend() {
        super("snapshot", TestSnapshot.class);
    }

}