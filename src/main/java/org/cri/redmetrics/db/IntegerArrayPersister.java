package org.cri.redmetrics.db;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;

public class IntegerArrayPersister extends StringType {

    private static final IntegerArrayPersister SINGLETON = new IntegerArrayPersister();

    private IntegerArrayPersister() {
        super(SqlType.OTHER, new Class<?>[]{Integer[].class});
    }

    public static IntegerArrayPersister getSingleton() {
        return SINGLETON;
    }


    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        Integer[] array = (Integer[]) javaObject;

        if (array == null) {
            return null;
        } else {
            String join = "";
            for (Integer i : array) {
                join += i + ",";
            }
            return "{" + join.substring(0, join.length() - 1) + "}";
        }

    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        String string = (String) sqlArg;

        if (string == null) {
            return null;
        } else {
            String[] strings = string.replaceAll("[{}]", "").split(",");
            Integer[] ints = new Integer[strings.length];
            for (int i = 0, length = strings.length; i < length; i++) {
                ints[i] = Integer.parseInt(strings[i]);
            }
            return ints;
        }
    }
}