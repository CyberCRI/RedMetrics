package org.cri.redmetrics.dao;

import com.google.inject.Inject;
import com.j256.ormlite.support.ConnectionSource;
import org.cri.redmetrics.model.Player;

import java.sql.SQLException;
import java.util.List;

public class PlayerDao extends EntityDao<Player> {
    
    @Inject
    public PlayerDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Player.class);
    }

    public Player findByEmail(String email) {
        try {
            if (email == null) return null;
            List<Player> players = orm.queryForEq("email", email);
            assert players.size() <= 1; // email should be unique
            if (players.isEmpty()) return null;
            else return players.get(0);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

}
