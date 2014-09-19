package org.cri.redmetrics.db;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;

public class StringArrayPersister extends StringType {

    private static final StringArrayPersister SINGLETON = new StringArrayPersister();

    private StringArrayPersister() {
        super(SqlType.OTHER, new Class<?>[]{String[].class});
    }

    public static StringArrayPersister getSingleton() {
        return SINGLETON;
    }


    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        String[] array = (String[]) javaObject;

        if (array == null) {
            return null;
        } else {
            String join = "";
            for (String str : array) {
                join += str + ",";
            }
            return "'{" + join.substring(0, join.length() - 1) + "}'";
        }

    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        String string = (String) sqlArg;

        if (string == null) {
            return null;
        } else {
            return string.replaceAll("[{}]", "").split(",");
        }
    }
}