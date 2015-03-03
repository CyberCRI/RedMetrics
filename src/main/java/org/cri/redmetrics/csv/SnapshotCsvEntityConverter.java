package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.inject.Inject;
import org.cri.redmetrics.dao.PlayerDao;
import org.cri.redmetrics.model.Event;
import org.cri.redmetrics.model.Snapshot;
import org.cri.redmetrics.util.DateFormatter;

/**
 * Created by himmelattack on 12/02/15.
 */
public class SnapshotCsvEntityConverter implements CsvEntityConverter<Snapshot> {
    private PlayerDao playerDao;

    @Inject
    public SnapshotCsvEntityConverter(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    public void writeHeader(CSVWriter csvWriter) {
        csvWriter.writeNext(new String[]{
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
                "section",
                "customData" });
    }

    @Override
    public void writeDataLine(CSVWriter csvWriter, Snapshot model) {
        // We need to include additional fields of the player
        // OPT: does this force a new request each time? if so we should join automatically
        playerDao.refresh(model.getPlayer());

        csvWriter.writeNext(new String[]{
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
                model.getSections(),
                model.getCustomData()
        });
    }
}
