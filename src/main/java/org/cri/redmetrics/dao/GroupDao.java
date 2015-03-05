
package org.cri.redmetrics.dao;

import com.google.inject.Inject;
import com.j256.ormlite.support.ConnectionSource;
import org.cri.redmetrics.model.Group;

import java.sql.SQLException;

/**
 *
 * @author Besnard Arthur
 */

public class GroupDao extends EntityDao<Group>{

    @Inject
    public GroupDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Group.class);
    }
}
