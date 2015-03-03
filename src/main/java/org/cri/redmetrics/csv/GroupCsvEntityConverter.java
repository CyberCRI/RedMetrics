package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.csv.CsvEntityConverter;
import org.cri.redmetrics.model.Game;
import org.cri.redmetrics.model.Group;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by himmelattack on 12/02/15.
 */
public class GroupCsvEntityConverter implements CsvEntityConverter<Group> {

    @Override
    public void writeHeader(CSVWriter csvWriter) {
        csvWriter.writeNext(new String[]{ "id", "name", "description", "creator", "open" });
    }

    @Override
    public void writeDataLine(CSVWriter csvWriter, Group model) {
        csvWriter.writeNext(new String[]{
                model.getId().toString(),
                model.getName(),
                model.getDescription(),
                model.getCreator(),
                CsvHelper.formatBoolean(model.isOpen())
        });
    }

}
