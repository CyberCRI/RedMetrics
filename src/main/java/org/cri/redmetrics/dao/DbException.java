package org.cri.redmetrics.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.SQLException;

@Data
@EqualsAndHashCode(callSuper = false)
public class DbException extends RuntimeException {

    private final SQLException sqlException;

}
