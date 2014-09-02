package org.cri.redmetrics.dao;

import com.google.inject.Inject;
import com.j256.ormlite.support.ConnectionSource;
import org.cri.redmetrics.model.Game;

import java.sql.SQLException;
import java.util.UUID;

public class GameDao extends EntityDao<Game> {

    @Inject
    public GameDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Game.class);
    }

    @Override
    public Game create(Game game) {
        if (game.getName() == null || game.getName().isEmpty())
            throw new InconsistentDataException("Game name is required");
        game.setAdminKey(UUID.randomUUID());
        return super.create(game);
    }

}
