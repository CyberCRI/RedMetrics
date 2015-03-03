package org.cri.redmetrics.csv;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.inject.Inject;
import org.cri.redmetrics.dao.PlayerDao;
import org.cri.redmetrics.model.Event;
import org.cri.redmetrics.util.DateFormatter;

import java.util.Arrays;
import java.util.stream.Collectors;

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
                "type",
                "coordinates",
                "section",
                "customData"
        });
    }

    @Override
    public void writeDataLine(CSVWriter csvWriter, Event model) {
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
                model.getType(),
                CsvHelper.formatCoordinates(model.getCoordinates()),
                model.getSections(),
                model.getCustomData()
        });
    }
}
