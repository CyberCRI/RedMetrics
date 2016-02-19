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

    // TODO: remove this unused method?
    @Override
    public String render(Object model) {
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
                writeEntity(csvWriter, (E) data);
                break;
            case ENTITY_LIST_OR_RESULTS_PAGE:
                if(data instanceof List)
                    csvEntityConverter.write(csvWriter, (List<E>) data);
                else
                    csvEntityConverter.write(csvWriter, ((ResultsPage<E>) data).results);
                break;
            case ENTITY_OR_ID_LIST:
                if(data instanceof Entity) {
                    writeEntity(csvWriter, (E) data);
                } else {
                    writeIdList(csvWriter, (UUID[]) data);
                }
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

    // Write single column of ids
    public void writeIdList(CSVWriter csvWriter, UUID[] uuidList) {
        csvWriter.writeNext(new String[]{ "id" });
        Arrays.stream(uuidList).forEach((uuid) -> csvWriter.writeNext(new String[]{ uuid.toString() }));
    }

    public void writeEntity(CSVWriter csvWriter, E entity) {
        List<E> singleElementList = new ArrayList<E>();
        singleElementList.add(entity);

        csvEntityConverter.write(csvWriter, singleElementList);
    }

}
