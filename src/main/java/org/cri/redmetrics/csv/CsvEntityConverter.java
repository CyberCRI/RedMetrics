package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.model.Entity;

/**
 * Created by himmelattack on 11/02/15.
 */
public interface CsvEntityConverter<E extends Entity> {

    public void writeHeader(CSVWriter csvWriter);

    public void writeDataLine(CSVWriter csvWriter, E model);

}
