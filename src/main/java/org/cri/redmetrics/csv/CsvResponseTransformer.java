package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.model.Entity;
import org.cri.redmetrics.model.ResultsPage;
import spark.ResponseTransformer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by himmelattack on 11/02/15.
 */
public class CsvResponseTransformer<E extends Entity> implements ResponseTransformer {

    CsvEntityConverter<E> csvEntityConverter;

    public CsvResponseTransformer(CsvEntityConverter<E> csvEntityConverter) {
        this.csvEntityConverter = csvEntityConverter;
    }

    @Override
    public String render(Object model) {
        // Write into a String
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter); // Defaults to CSV with double-quotes

        // Depending on the data passed, call the right method to serialize it
        if (model instanceof ResultsPage) {
            stringifyResultsPage(csvWriter, (ResultsPage<E>) model);
        } else if(model instanceof UUID[]) {
            stringifyUuidList(csvWriter, (UUID[]) model);
        } else {
            stringifyEntity(csvWriter, (E) model);
        }

        // Return whatever the StringWriter has buffered
        try {
            csvWriter.close();
            return stringWriter.toString();
        } catch(IOException e) {
            throw new RuntimeException("Cannot write to CSV", e);
        }
    }

    private void stringifyResultsPage(CSVWriter csvWriter, ResultsPage<E> resultsPage) {
        csvEntityConverter.write(csvWriter, resultsPage.results);
    }

    // Write single column of ids
    private void stringifyUuidList(CSVWriter csvWriter, UUID[] uuidList) {
        csvWriter.writeNext(new String[]{ "id" });
        Arrays.stream(uuidList).forEach((uuid) -> csvWriter.writeNext(new String[]{ uuid.toString() }));

    }

    private void stringifyEntity(CSVWriter csvWriter, E entity) {
        List<E> singleElementList = new ArrayList<E>();
        singleElementList.add(entity);

        csvEntityConverter.write(csvWriter, singleElementList);
    }
}
