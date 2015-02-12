package org.cri.redmetrics.csv;

import org.cri.redmetrics.model.Entity;

/**
 * Created by himmelattack on 11/02/15.
 */
public interface CsvEntityConverter<E extends Entity> {

    public void writeToCsv(E model);

}
