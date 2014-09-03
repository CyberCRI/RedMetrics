package org.cri.redmetrics.dao;

import com.google.inject.Inject;
import com.j256.ormlite.support.ConnectionSource;
import org.cri.redmetrics.model.Snapshot;

import java.sql.SQLException;

public class SnapshotDao extends EntityDao<Snapshot> {

    @Inject
    public SnapshotDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Snapshot.class);
    }

}
