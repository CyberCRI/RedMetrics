package org.cri.redmetrics.dao;

import com.google.inject.Inject;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import org.cri.redmetrics.model.Address;

public class AddressDao extends EntityDao<Address> {

    @Inject
    public AddressDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Address.class);
    }
}
