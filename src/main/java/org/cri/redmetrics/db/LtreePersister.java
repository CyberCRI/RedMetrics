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
        String ltree = (String) javaObject;

        if (ltree == null) {
            return null;
        } else {
            return ltree.trim();
        }

    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        String ltree = (String) sqlArg;

        if (ltree == null) {
            return null;
        } else {
            return ltree;
        }
    }
}