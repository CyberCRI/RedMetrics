package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.model.GameVersion;

/**
 * Created by himmelattack on 12/02/15.
 */
public class GameVersionCsvEntityConverter implements CsvEntityConverter<GameVersion> {


    @Override
    public void writeHeader(CSVWriter csvWriter) {
        csvWriter.writeNext(new String[]{ "id", "name", "description", "customData" });
    }

    @Override
    public void writeDataLine(CSVWriter csvWriter, GameVersion model) {
        csvWriter.writeNext(new String[]{ model.getId().toString(), model.getName(), model.getDescription(),
                model.getCustomData() });
    }
}

