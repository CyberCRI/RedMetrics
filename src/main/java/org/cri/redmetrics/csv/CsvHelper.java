package org.cri.redmetrics.csv;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oracle.javafx.jmx.json.JSONReader;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.cri.redmetrics.model.Entity;
import org.cri.redmetrics.model.Gender;
import org.cri.redmetrics.util.DateFormatter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    public static class UnpackedCustomData {
        public Set<String> columnNames = new HashSet<>();
        public List<Map<String, String>> rowValues = new ArrayList<>();
    }

    public static UnpackedCustomData unpackCustomData(List<? extends Entity> entityList) {
        UnpackedCustomData unpackedCustomData = new UnpackedCustomData();
        entityList.forEach(entity -> {
            Map<String, String> rowValues = new HashMap<String, String>();
            JsonElement jsonElement = new JsonParser().parse(entity.getCustomData());
            if(jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                jsonObject.entrySet().forEach(entry -> {
                    String columnName = "customData." + entry.getKey();
                    unpackedCustomData.columnNames.add(columnName);
                    rowValues.put(columnName, entry.getValue().toString());
                });
            }
            else
            {
                unpackedCustomData.columnNames.add("customData");
                rowValues.put("customData", entity.getCustomData());
            }

            unpackedCustomData.rowValues.add(rowValues);
        });

        return unpackedCustomData;
    }

    public static String[] concatenateArrays(String[] a, String[] b) {
        return (String[]) Stream.concat(Arrays.stream(a), Arrays.stream(b)).toArray();

    }
}
