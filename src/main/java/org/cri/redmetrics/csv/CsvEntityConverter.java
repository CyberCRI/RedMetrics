package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.model.Entity;

import java.util.List;

/**
 * Created by himmelattack on 11/02/15.
 */
public interface CsvEntityConverter<E extends Entity> {

    public void write(CSVWriter csvWriter, List<E> models);

}
