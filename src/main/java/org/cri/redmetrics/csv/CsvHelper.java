package org.cri.redmetrics.csv;

import org.cri.redmetrics.model.Gender;
import org.cri.redmetrics.util.DateFormatter;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by himmelattack on 03/03/15.
 */
public class CsvHelper {

    public static String formatDate(Date date) {
        return date != null ? DateFormatter.print(date) : null;
    }

    public static String formatCoordinates(Integer[] coordinates) {
        if(coordinates == null) return null;

        // Write out coordinates in JSON array format
        return "[" + Arrays.stream(coordinates).map(x -> x.toString()).collect(Collectors.joining(", ")) + "]";
    }

    public static String formatBoolean(boolean bool) {
        return bool ? "true" : "false";
    }

    public static String formatGender(Gender gender) {
        return gender != null ? gender.name() : null;
    }
}
