package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import org.cri.redmetrics.model.Player;
import org.cri.redmetrics.util.DateFormatter;

/**
 * Created by himmelattack on 12/02/15.
 */
public class PlayerCsvEntityConverter implements CsvEntityConverter<Player> {

    @Override
    public void writeHeader(CSVWriter csvWriter) {
        csvWriter.writeNext(new String[]{ "id", "birthDate", "postalCode", "country", "gender", "externalId", "customData" });
    }

    @Override
    public void writeDataLine(CSVWriter csvWriter, Player model) {
        csvWriter.writeNext(new String[]{
                model.getId().toString(),
                model.getBirthDate() != null ? DateFormatter.print(model.getBirthDate()) : null,
                model.getPostalCode(),
                model.getCountry(),
                model.getGender() != null ? model.getGender().name() : null,
                model.getExternalId(),
                model.getCustomData()
        });
    }

}
