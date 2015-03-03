package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.model.Player;
import org.cri.redmetrics.util.DateFormatter;

import java.util.List;

/**
 * Created by himmelattack on 12/02/15.
 */
public class PlayerCsvEntityConverter implements CsvEntityConverter<Player> {

    @Override
    public void write(CSVWriter csvWriter, List<Player> models) {
        csvWriter.writeNext(new String[]{
                "id",
                "birthDate",
                "region",
                "country",
                "gender",
                "externalId",
                "customData"
        });

        for(Player model : models) {
            csvWriter.writeNext(new String[]{
                    model.getId().toString(),
                    CsvHelper.formatDate(model.getBirthDate()),
                    model.getRegion(),
                    model.getCountry(),
                    model.getGender() != null ? model.getGender().name() : null,
                    model.getExternalId(),
                    model.getCustomData()
            });
        }
    }

}
