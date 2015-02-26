package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.model.Game;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by himmelattack on 12/02/15.
 */
public class GameCsvEntityConverter implements CsvEntityConverter<Game> {


    @Override
    public void writeHeader(CSVWriter csvWriter) {
        csvWriter.writeNext(new String[]{ "id", "name", "author", "description", "customData" });
    }

    @Override
    public void writeDataLine(CSVWriter csvWriter, Game model) {
        csvWriter.writeNext(new String[]{ model.getId().toString(), model.getName(), model.getAuthor(),
                model.getDescription(), model.getCustomData() });
    }
}
