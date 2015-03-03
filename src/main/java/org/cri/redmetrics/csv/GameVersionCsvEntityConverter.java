package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.model.GameVersion;

import java.util.List;

/**
 * Created by himmelattack on 12/02/15.
 */
public class GameVersionCsvEntityConverter implements CsvEntityConverter<GameVersion> {


    @Override
    public void write(CSVWriter csvWriter, List<GameVersion> models) {
        csvWriter.writeNext(new String[]{ "id", "name", "description", "customData" });
        for(GameVersion model : models) {
            csvWriter.writeNext(new String[]{model.getId().toString(), model.getName(),
                    model.getDescription(), model.getCustomData()});
        }
    }
}

