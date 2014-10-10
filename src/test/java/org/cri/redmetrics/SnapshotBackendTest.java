package org.cri.redmetrics;

import org.cri.redmetrics.backend.Backends;
import org.cri.redmetrics.backend.SnapshotBackend;
import org.cri.redmetrics.model.TestGameVersion;
import org.cri.redmetrics.model.TestPlayer;
import org.cri.redmetrics.model.TestSnapshot;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.fest.assertions.api.Assertions.assertThat;

public class SnapshotBackendTest {

    static final SnapshotBackend snapshots = Backends.SNAPSHOT;

    TestGameVersion gameVersion;
    TestPlayer player;
    TestSnapshot snapshot;

    public void resetGameVersion() throws IOException {
        gameVersion = Backends.newSavedGameVersion();
        snapshot.setGameVersion(gameVersion.getId());
    }

    public void resetPlayer() throws IOException {
        player = Backends.newSavedPlayer();
        snapshot.setPlayer(player.getId());
    }

    @BeforeTest
    public void resetSnapshot() throws IOException {
        snapshot = new TestSnapshot();
        resetGameVersion();
        resetPlayer();
    }

    @Test
    public void test() throws IOException {
        snapshot = snapshots.post(snapshot);
        assertThat(snapshot).isNotNull();
        assertThat(snapshot.getId()).isNotNull().hasSize(36);
        assertThat(snapshot.getGameVersion()).isNotNull();
    }
}