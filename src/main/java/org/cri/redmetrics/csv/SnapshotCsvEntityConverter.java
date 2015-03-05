package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.inject.Inject;
import org.cri.redmetrics.dao.PlayerDao;
import org.cri.redmetrics.model.Event;
import org.cri.redmetrics.model.Snapshot;
import org.cri.redmetrics.util.DateFormatter;

import java.util.List;
import java.util.Map;

/**
 * Created by himmelattack on 12/02/15.
 */
public class SnapshotCsvEntityConverter implements CsvEntityConverter<Snapshot> {
    private PlayerDao playerDao;

    @Inject
    public SnapshotCsvEntityConverter(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @Override
    public void write(CSVWriter csvWriter, List<Snapshot> models) {
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
                "section"
        };

        CsvHelper.UnpackedCustomData unpackedCustomData = CsvHelper.unpackCustomData(models);

        String[] customDataColumnNames = unpackedCustomData.columnNames.toArray(new String[0]);
        String[] allColumnNames = CsvHelper.concatenateArrays(staticColumnNames, customDataColumnNames);

        csvWriter.writeNext(allColumnNames);

        // Write out rows
        for(int i = 0; i < models.size(); i++) {
            Snapshot model = models.get(i);

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
                    model.getSections()
            };

            // Write out the custom data in order, skipping fields with no data
            Map<String, String> rowValues = unpackedCustomData.rowValues.get(i);
            String[] customDataRowValues = unpackedCustomData.columnNames.stream().map(columnName -> rowValues.get(columnName)).toArray(String[]::new);
            String[] allRowValues = CsvHelper.concatenateArrays(staticRowValues, customDataRowValues);

            csvWriter.writeNext(allRowValues);
        }
    }
}
