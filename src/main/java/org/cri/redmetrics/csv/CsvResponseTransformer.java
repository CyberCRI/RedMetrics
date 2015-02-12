package org.cri.redmetrics.csv;

import org.cri.redmetrics.model.Entity;
import org.cri.redmetrics.model.ResultsPage;
import spark.ResponseTransformer;

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
        if (model instanceof ResultsPage) {
            return stringifyResultsPage((ResultsPage<E>) model);
        } else if(model instanceof UUID[]) {
            return stringifyUuidList((UUID[]) model);
        } else {
            return stringifyEntity((E) model);
        }
    }


    private String stringifyResultsPage(ResultsPage<E> resultsPage) {
        // TODO
        resultsPage.results.forEach((entity) -> csvEntityConverter.writeToCsv(entity));
        return "results page";
    }

    private String stringifyUuidList(UUID[] uuidList) {
        // TODO
        return "uuid list";
    }

    private String stringifyEntity(E entity) {
        // TODO
        csvEntityConverter.writeToCsv(entity);
        return "entity";
    }

}
