package org.cri.redmetrics.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.cri.redmetrics.model.GameVersion;
import org.cri.redmetrics.model.Player;
import org.cri.redmetrics.model.ProgressData;

import java.util.UUID;

public class ProgressDataJsonConverter<E extends ProgressData> extends EntityJsonConverter<E> {

    ProgressDataJsonConverter(Class<E> entityType, Gson gson, JsonParser jsonParser) {
        super(entityType, gson, jsonParser);
    }

    @Override
    public E parse(String json) {
        E progressData = super.parse(json);
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        // GAME VERSION
        JsonElement gameVersionId = jsonObject.get("gameVersion");
        if (gameVersionId != null) {
            GameVersion gameVersion = new GameVersion();
            // TODO Make sure the progress data can update without overwriting gameVersion data
            gameVersion.setId(UUID.fromString(gameVersionId.getAsString()));
            progressData.setGameVersion(gameVersion);
        }

        // PLAYER
        JsonElement playerId = jsonObject.get("player");
        if (playerId != null) {
            Player player = new Player();
            // TODO Make sure the progress data can update without overwriting player data
            player.setId(UUID.fromString(playerId.getAsString()));
            progressData.setPlayer(player);
        }

        // CUSTOM DATA
        JsonElement data = jsonObject.get("customData");
        if (data != null) {
            progressData.setCustomData(data.toString()); // Make customData a String so it can be persisted in SQL Database
        }

        return progressData;
    }

    @Override
    public JsonObject toJsonObject(ProgressData progressData) {
        JsonObject progressDataJson = gson.toJsonTree(progressData).getAsJsonObject();

        // GAME
        progressDataJson.addProperty("gameVersion", progressData.getGameVersion().getId().toString());

        // PLAYER
        progressDataJson.addProperty("player", progressData.getPlayer().getId().toString());

        // CUSTOM DATA
        if (progressData.getCustomData() != null) {
            JsonElement dataJson = jsonParser.parse(progressData.getCustomData()); // Transform saved customData field back to its original form
            progressDataJson.add("customData", dataJson);
        }

        return progressDataJson;
    }

}
