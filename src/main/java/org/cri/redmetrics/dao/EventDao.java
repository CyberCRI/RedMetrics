package org.cri.redmetrics.dao;

import com.google.inject.Inject;
import com.j256.ormlite.support.ConnectionSource;
import org.cri.redmetrics.model.Event;

import java.sql.SQLException;

public class EventDao extends EntityDao<Event> {

    @Inject
    public EventDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Event.class);
    }

}
