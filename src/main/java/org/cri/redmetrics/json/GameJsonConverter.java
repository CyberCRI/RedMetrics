package org.cri.redmetrics.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.cri.redmetrics.model.Game;
import java.util.ArrayList;
import java.util.Collection;

public class GameJsonConverter extends EntityJsonConverter<Game> {

    @Inject
    GameJsonConverter(Gson gson, JsonParser jsonParser) {
        super(Game.class, new TypeToken<Collection<Game>>(){}.getType(), gson, jsonParser);
    }

}
