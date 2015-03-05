package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.model.Group;

import java.util.List;

/**
 * Created by himmelattack on 12/02/15.
 */
public class GroupCsvEntityConverter implements CsvEntityConverter<Group> {

    @Override
    public void write(CSVWriter csvWriter, List<Group> models) {
        csvWriter.writeNext(new String[]{ "id", "name", "description", "creator", "open" });
        for(Group model : models) {
            csvWriter.writeNext(new String[]{
                    model.getId().toString(),
                    model.getName(),
                    model.getDescription(),
                    model.getCreator(),
                    CsvHelper.formatBoolean(model.isOpen())
            });
        }
    }

}
