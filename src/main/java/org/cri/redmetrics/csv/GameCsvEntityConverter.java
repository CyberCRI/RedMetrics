package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.model.Game;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by himmelattack on 12/02/15.
 */
public class GameCsvEntityConverter implements CsvEntityConverter<Game> {

    @Override
    public void write(CSVWriter csvWriter, List<Game> models) {
        csvWriter.writeNext(new String[]{ "id", "name", "author", "description", "customData" });
        for(Game model : models) {
            csvWriter.writeNext(new String[]{ model.getId().toString(), model.getName(), model.getAuthor(),
                    model.getDescription(), model.getCustomData() });
        }
    }
}
