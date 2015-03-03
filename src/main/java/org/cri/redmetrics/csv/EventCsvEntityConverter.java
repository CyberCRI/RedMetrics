package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.model.Event;
import org.cri.redmetrics.util.DateFormatter;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by himmelattack on 12/02/15.
 */
public class EventCsvEntityConverter implements CsvEntityConverter<Event> {

    @Override
    public void writeHeader(CSVWriter csvWriter) {
        csvWriter.writeNext(new String[]{ "id", "serverTime", "userTime", "gameVersion", "type", "coordinates", "section", "customData" });
    }

    @Override
    public void writeDataLine(CSVWriter csvWriter, Event model) {
        // Write out coordinates in JSON array format
        String coordinates = null;
        if(model.getCoordinates() != null) {
            coordinates = "[" + Arrays.stream(model.getCoordinates()).map(x -> x.toString()).collect(Collectors.joining(", ")) + "]";
        }

        csvWriter.writeNext(new String[]{
                model.getId().toString(),
                model.getServerTime() != null ? DateFormatter.print(model.getServerTime()) : null,
                model.getUserTime() != null ? DateFormatter.print(model.getUserTime()) : null,
                model.getGameVersion().getId().toString(),
                model.getType(),
                coordinates,
                model.getSections(),
                model.getCustomData()
        });
    }
}
