package org.cri.redmetrics.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.cri.redmetrics.model.Game;
import org.cri.redmetrics.model.GameVersion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class GameVersionJsonConverter extends EntityJsonConverter<GameVersion> {

    @Inject
    GameVersionJsonConverter(@Named("GameVersion") Gson gson, JsonParser jsonParser) {
        super(GameVersion.class, gson, jsonParser);
    }

    @Override
    public GameVersion parse(String json) {
        GameVersion gameVersion = super.parse(json);
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        // GAME
        JsonElement gameId = jsonObject.get("game");
        if (gameId != null) {
            Game game = new Game();
            // TODO Make sure the game version can update without overwriting game data
            game.setId(UUID.fromString(gameId.getAsString()));
            gameVersion.setGame(game);
        }

        return gameVersion;
    }

    @Override
    public JsonObject toJsonObject(GameVersion gameVersion) {
        JsonObject progressDataJson = gson.toJsonTree(gameVersion).getAsJsonObject();

        // GAME
        progressDataJson.addProperty("game", gameVersion.getGame().getId().toString());

        return progressDataJson;
    }

}
