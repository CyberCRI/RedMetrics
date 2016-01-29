package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.model.Entity;
import org.cri.redmetrics.model.ResultsPage;
import org.cri.redmetrics.util.RouteHelper;
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
        /*StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter); // Defaults to CSV with double-quotes

        // Depending on the data passed, call the right method to serialize it
        if (model instanceof ResultsPage) {
            renderResultsPage(csvWriter, (ResultsPage<E>) model);
        } else if(model instanceof List) {
            renderEntityList(csvWriter, (List<E>) model);
        } else if(model instanceof UUID[]) {
            renderIdList(csvWriter, (UUID[]) model);
        } else {
            renderEntity(csvWriter, (E) model);
        }

        // Return whatever the StringWriter has buffered
        try {
            csvWriter.close();
            return stringWriter.toString();
        } catch(IOException e) {
            throw new RuntimeException("Cannot write to CSV", e);
        }*/
        return "";
    }

    public String render(RouteHelper.DataType dataType, Object data) {
        // Write into a String
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter); // Defaults to CSV with double-quotes

        // Depending on the data passed, call the right method to serialize it
        // All data needs to be converted into lists
        switch(dataType) {
            case ENTITY:
                List<E> singleElementList = new ArrayList<E>();
                singleElementList.add((E) data);

                csvEntityConverter.write(csvWriter, singleElementList);
                break;
            case ENTITY_LIST:
                csvEntityConverter.write(csvWriter, (List<E>) data);
                break;
            case ENTITY_RESULTS_PAGE:
                csvEntityConverter.write(csvWriter,  ((ResultsPage<E>) data).results);
                break;
            case ID_LIST:
                csvWriter.writeNext(new String[]{ "id" });
                Arrays.stream((UUID[]) data).forEach((uuid) -> csvWriter.writeNext(new String[]{ uuid.toString() }));
                break;
        }

        // Return whatever the StringWriter has buffered
        try {
            csvWriter.close();
            return stringWriter.toString();
        } catch(IOException e) {
            throw new RuntimeException("Cannot write to CSV", e);
        }
    }

    /*public String renderResultsPage(ResultsPage<E> resultsPage) {
        csvEntityConverter.write(csvWriter, resultsPage.results);
    }

    public String renderEntityList(CSVWriter csvWriter, List<E> results) {
        csvEntityConverter.write(csvWriter, results);
    }

    // Write single column of ids
    public String renderIdList(CSVWriter csvWriter, UUID[] uuidList) {
        csvWriter.writeNext(new String[]{ "id" });
        Arrays.stream(uuidList).forEach((uuid) -> csvWriter.writeNext(new String[]{ uuid.toString() }));

    }

    public String renderEntity(CSVWriter csvWriter, E entity) {
        List<E> singleElementList = new ArrayList<E>();
        singleElementList.add(entity);

        csvEntityConverter.write(csvWriter, singleElementList);
    }*/

}
