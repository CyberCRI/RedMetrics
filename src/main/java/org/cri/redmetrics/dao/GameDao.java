package org.cri.redmetrics.dao;

import com.google.inject.Inject;
import com.j256.ormlite.support.ConnectionSource;
import org.cri.redmetrics.model.Game;

import java.sql.SQLException;
import java.util.UUID;

public class GameDao extends EntityDao<Game> {

    @Inject
    public GameDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource);
    }

    @Override
    protected Class<Game> getEntityType() {
        return Game.class;
    }

    @Override
    public Game create(Game game) {
        game.setApiKey(UUID.randomUUID());
        return super.create(game);
    }

}
