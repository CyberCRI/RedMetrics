package org.cri.redmetrics.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.cri.redmetrics.model.Player;

import java.util.Collection;

public class PlayerJsonConverter extends EntityJsonConverter<Player> {

    @Inject
    PlayerJsonConverter(Gson gson, JsonParser jsonParser) {
        super(Player.class, new TypeToken<Collection<Player>>(){}.getType(), gson, jsonParser);
    }

}
