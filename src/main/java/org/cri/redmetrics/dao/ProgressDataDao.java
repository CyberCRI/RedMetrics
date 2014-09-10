package org.cri.redmetrics.dao;

import com.j256.ormlite.support.ConnectionSource;
import org.cri.redmetrics.model.ProgressData;

import java.sql.SQLException;

public class ProgressDataDao<E extends ProgressData> extends EntityDao<E> {

    ProgressDataDao(ConnectionSource connectionSource, Class<E> type) throws SQLException {
        super(connectionSource, type);
    }

}