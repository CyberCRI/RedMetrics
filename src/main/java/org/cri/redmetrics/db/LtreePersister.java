package org.cri.redmetrics.db;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;

public class LtreePersister extends StringType {

    private static final LtreePersister SINGLETON = new LtreePersister();

    private LtreePersister() {
        super(SqlType.OTHER, new Class<?>[]{String[].class});
    }

    public static LtreePersister getSingleton() {
        return SINGLETON;
    }


    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        return javaObject;
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return sqlArg;
    }
}