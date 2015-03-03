package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.inject.Inject;
import org.cri.redmetrics.dao.PlayerDao;
import org.cri.redmetrics.model.Event;
import org.cri.redmetrics.util.DateFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by himmelattack on 12/02/15.
 */
public class EventCsvEntityConverter implements CsvEntityConverter<Event> {
    private PlayerDao playerDao;

    @Inject
    public EventCsvEntityConverter(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @Override
    public void write(CSVWriter csvWriter, List<Event> models) {
        String[] staticColumnNames = {
                "id",
                "serverTime",
                "userTime",
                "gameVersion",
                "playerId",
                "playerBirthdate",
                "playerRegion",
                "playerCountry",
                "playerGender",
                "playerExternalId",
                "playerCustomData",
                "type",
                "coordinates",
                "section",
        };

        CsvHelper.UnpackedCustomData unpackedCustomData = CsvHelper.unpackCustomData(models);

        String[] customDataColumnNames = (String[]) unpackedCustomData.columnNames.toArray();
        String[] allColumnNames = CsvHelper.concatenateArrays(staticColumnNames, customDataColumnNames);

        csvWriter.writeNext(allColumnNames);

        // Write out rows
        for(int i = 0; i < models.size(); i++) {
            Event model = models.get(i);

            // We need to include additional fields of the player
            // OPT: does this force a new request each time? if so we should join automatically
            playerDao.refresh(model.getPlayer());

            String[] staticRowValues = {
                    model.getId().toString(),
                    CsvHelper.formatDate(model.getServerTime()),
                    CsvHelper.formatDate(model.getUserTime()),
                    model.getGameVersion().getId().toString(),
                    model.getPlayer().getId().toString(),
                    CsvHelper.formatDate(model.getPlayer().getBirthDate()),
                    model.getPlayer().getRegion(),
                    model.getPlayer().getCountry(),
                    CsvHelper.formatGender(model.getPlayer().getGender()),
                    model.getPlayer().getExternalId(),
                    model.getPlayer().getCustomData(),
                    model.getType(),
                    CsvHelper.formatCoordinates(model.getCoordinates()),
                    model.getSections(),
                    model.getCustomData()
            };

            // Write out the custom data in order, skipping fields with no data
            Map<String, String> rowValues = unpackedCustomData.rowValues.get(i);
            String[] customDataRowValues = (String[]) unpackedCustomData.columnNames.stream().map(columnName -> rowValues.get(columnName)).toArray();
            String[] allRowValues = CsvHelper.concatenateArrays(staticRowValues, customDataRowValues);

            csvWriter.writeNext(allRowValues);
        }
    }
}
