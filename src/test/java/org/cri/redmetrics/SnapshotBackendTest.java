package org.cri.redmetrics;

import org.cri.redmetrics.backend.SnapshotBackend;
import org.cri.redmetrics.model.TestSnapshot;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.fest.assertions.api.Assertions.assertThat;

public class SnapshotBackendTest extends ProgressDataBackendTest {

    static final SnapshotBackend snapshots = new SnapshotBackend();

    TestSnapshot snapshot;

    @BeforeTest
    void createSnapshot() throws IOException {
        snapshot = new TestSnapshot();
        snapshot.setGame(game.getId());
        snapshot.setPlayer(player.getId());
    }

    @Test
    public void test() throws IOException {
        snapshot = snapshots.post(snapshot);
        assertThat(snapshot).isNotNull();
        assertThat(snapshot.getGame()).isGreaterThan(0);
    }
}