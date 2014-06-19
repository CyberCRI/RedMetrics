package org.cri.redmetrics.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.cri.redmetrics.model.Event;
import org.cri.redmetrics.model.Game;
import org.cri.redmetrics.model.Player;

public class EventJson extends EntityJson<Event> {

    @Inject
    EventJson(@Named("Event") Gson gson, JsonParser jsonParser) {
        super(Event.class, gson, jsonParser);
    }

    @Override
    public Event parse(String json) {
        Event event = super.parse(json);
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        // DATA
        JsonElement data = jsonObject.get("data");
        if (data != null) {
            event.setData(data.toString()); // Make data a String so it can be persisted in SQL Database
        }

        // GAME
        JsonElement gameId = jsonObject.get("game");
        if (gameId != null) {
            Game game = new Game();
            game.setId(gameId.getAsInt());
            event.setGame(game);
        }

        // PLAYER
        JsonElement playerId = jsonObject.get("player");
        if (playerId != null) {
            Player player = new Player();
            player.setId(playerId.getAsInt());
            event.setPlayer(player);
        }

        return event;
    }

    @Override
    public String stringify(Event event) {
        JsonObject eventJson = gson.toJsonTree(event).getAsJsonObject();

        // GAME
        eventJson.addProperty("game", event.getGame().getId());

        // PLAYER
        eventJson.addProperty("player", event.getPlayer().getId());

        // DATA
        if (event.getData() != null) {
            JsonElement dataJson = jsonParser.parse(event.getData()); // Transform saved data field back to its original form
            eventJson.add("data", dataJson);
        }

        return gson.toJson(eventJson);
    }

}
