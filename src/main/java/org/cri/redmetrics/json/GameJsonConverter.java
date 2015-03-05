package org.cri.redmetrics.json;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.cri.redmetrics.model.Game;

public class GameJsonConverter extends EntityJsonConverter<Game> {

    @Inject
    GameJsonConverter(Gson gson, JsonParser jsonParser) {
        super(Game.class, gson, jsonParser);
    }

}
