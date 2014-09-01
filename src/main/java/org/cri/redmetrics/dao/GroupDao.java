
package org.cri.redmetrics.dao;

import com.google.inject.Inject;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import org.cri.redmetrics.model.Group;

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
