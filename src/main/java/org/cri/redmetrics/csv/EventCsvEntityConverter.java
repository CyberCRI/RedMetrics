package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.model.Event;
import org.cri.redmetrics.util.DateFormatter;

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
        csvWriter.writeNext(new String[]{
                model.getId().toString(),
                model.getServerTime() != null ? DateFormatter.print(model.getServerTime()) : null,
                model.getUserTime() != null ? DateFormatter.print(model.getUserTime()) : null,
                model.getGameVersion().getId().toString(),
                model.getType(),
                model.getCoordinates() != null ? model.getCoordinates().toString() : null,
                model.getSections(),
                model.getCustomData()
        });
    }
}
