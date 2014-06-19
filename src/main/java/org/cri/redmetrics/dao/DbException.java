package org.cri.redmetrics.dao;

import lombok.Data;

import java.sql.SQLException;

@Data
public class DbException extends RuntimeException {

    private final SQLException sqlException;

}
