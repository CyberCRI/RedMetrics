package org.cri.redmetrics;

import org.cri.redmetrics.backend.Backends;
import org.cri.redmetrics.backend.SnapshotBackend;
import org.cri.redmetrics.model.TestGame;
import org.cri.redmetrics.model.TestPlayer;
import org.cri.redmetrics.model.TestSnapshot;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.fest.assertions.api.Assertions.assertThat;

public class SnapshotBackendTest {

    static final SnapshotBackend snapshots = Backends.SNAPSHOT;

    TestGame game = Backends.newSavedGame();
    TestPlayer player = Backends.newSavedPlayer();
    TestSnapshot snapshot;

    public void resetGame() throws IOException {
        game = Backends.newSavedGame();
        snapshot.setGame(game.getId());
    }

    public void resetPlayer() throws IOException {
        player = Backends.newSavedPlayer();
        snapshot.setPlayer(player.getId());
    }

    @BeforeTest
    public void createSnapshot() throws IOException {
        game = Backends.newSavedGame();
        player = Backends.newSavedPlayer();
        snapshot = new TestSnapshot();
        snapshot.setGame(game.getId());
        snapshot.setPlayer(player.getId());
    }

    @Test
    public void test() throws IOException {
        snapshot = snapshots.post(snapshot);
        assertThat(snapshot).isNotNull();
        assertThat(snapshot.getId()).isNotNull().hasSize(36);
        assertThat(snapshot.getGame()).isNotNull();
    }
}