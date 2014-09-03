package org.cri.redmetrics.backend;

import org.cri.redmetrics.model.TestSnapshot;

public class SnapshotBackend extends HttpBackend<TestSnapshot> {

    public SnapshotBackend() {
        super("snapshot/", TestSnapshot.class);
    }

}