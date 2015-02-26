package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.model.Event;
import org.cri.redmetrics.model.Snapshot;
import org.cri.redmetrics.util.DateFormatter;

/**
 * Created by himmelattack on 12/02/15.
 */
public class SnapshotCsvEntityConverter implements CsvEntityConverter<Snapshot> {

    public void writeHeader(CSVWriter csvWriter) {
        csvWriter.writeNext(new String[]{ "id", "serverTime", "userTime", "gameVersion", "section", "customData" });
    }

    @Override
    public void writeDataLine(CSVWriter csvWriter, Snapshot model) {
        csvWriter.writeNext(new String[]{
                model.getId().toString(),
                model.getServerTime() != null ? DateFormatter.print(model.getServerTime()) : null,
                model.getUserTime() != null ? DateFormatter.print(model.getUserTime()) : null,
                model.getGameVersion().getId().toString(),
                model.getSections(),
                model.getCustomData()
        });
    }
}
