package org.cri.redmetrics.backend;

import com.google.api.client.http.GenericUrl;
import org.cri.redmetrics.model.TestGameVersion;

import java.io.IOException;
import java.util.List;

public class GameVersionBackend extends HttpBackend<TestGameVersion> {

    GameVersionBackend() {
        super("gameVersion", TestGameVersion.class);
    }

    public List<TestGameVersion> getGameVersions(String gameId) throws IOException {
        GenericUrl url = new GenericUrl(BASE_PATH + "game/" + gameId + "/versions");
        return (List<TestGameVersion>) requestFactory.buildGetRequest(url).execute().parseAs(arrayType);
    }

}