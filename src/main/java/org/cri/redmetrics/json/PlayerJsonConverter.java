package org.cri.redmetrics.json;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.cri.redmetrics.model.Player;

public class PlayerJsonConverter extends EntityJsonConverter<Player> {

    @Inject
    PlayerJsonConverter(Gson gson, JsonParser jsonParser) {
        super(Player.class, gson, jsonParser);
    }

}
