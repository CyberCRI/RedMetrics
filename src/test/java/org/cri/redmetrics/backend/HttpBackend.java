package org.cri.redmetrics.backend;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Types;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.cri.redmetrics.Server;
import org.cri.redmetrics.controller.Controller;
import org.cri.redmetrics.model.TestEntity;
import org.cri.redmetrics.util.DateUtils;
import org.joda.time.DateTime;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpBackend<E extends TestEntity> {

    private static final int PORT_NUMBER = 7654;
    private static final Server server = new Server(PORT_NUMBER);
    private static final HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory((request) -> request.setParser(new JsonObjectParser(new GsonFactory())));

    private final String path;
    private final Class<E> type;
    private final Type arrayType;

    HttpBackend(String entityPath, Class<E> type) {
        this.path = "http://localhost:" + PORT_NUMBER + Controller.basePath + entityPath;
        this.type = type;
        this.arrayType = Types.getArrayComponentType(type);
        server.start();
    }

    public E getById(String id) throws IOException {
        return requestFactory.buildGetRequest(url("/" + id)).execute().parseAs(type);
    }

    public E post(E entity) throws IOException {
        HttpContent json = asJson(entity);
        return requestFactory.buildPostRequest(url("/"), json).execute().parseAs(type);
    }

    public E put(E entity) throws IOException {
        return put(entity.getId(), entity);
    }

    public E put(String id, E entity) throws IOException {
        HttpContent json = asJson(entity);
        return requestFactory.buildPutRequest(url("/" + id), json).execute().parseAs(type);
    }

    public E delete(String id) throws IOException {
        return requestFactory.buildDeleteRequest(url("/" + id)).execute().parseAs(type);
    }

    private HttpContent asJson(E entity) {
        return new JsonHttpContent(new GsonFactory(), entity);
    }

    private GenericUrl url() {
        return new GenericUrl(path);
    }

    private GenericUrl url(String relativePath) {
        return new GenericUrl(path + relativePath);
    }

    public SearchQueryBuilder search() {
        return new SearchQueryBuilder();
    }

    private List<E> search(GenericUrl url) throws IOException {
        return (List<E>) requestFactory.buildGetRequest(url).execute().parseAs(arrayType);
    }


    public class SearchQueryBuilder {

        Map<String, String> params = new HashMap<>();

        public SearchQueryBuilder with(String key, String value) {
            params.put(key, value);
            return this;
        }

        public SearchQueryBuilder withGame(String gameId) {
            return with("game", gameId);
        }

        public SearchQueryBuilder withGames(String... gameIds) {
            Preconditions.checkArgument(gameIds.length > 0);
            return with("game", Joiner.on(',').join(gameIds));
        }

        public SearchQueryBuilder withPlayer(String playerId) {
            return with("player", playerId);
        }

        public SearchQueryBuilder withPlayers(String... playerIds) {
            Preconditions.checkArgument(playerIds.length > 0);
            return with("player", Joiner.on(',').join(playerIds));
        }

        public SearchQueryBuilder withType(String type) {
            return with("type", type);
        }

        public SearchQueryBuilder before(DateTime date) {
            return with("before", DateUtils.print(date));
        }

        public SearchQueryBuilder after(DateTime date) {
            return with("after", DateUtils.print(date));
        }

        public SearchQueryBuilder beforeUserTime(DateTime date) {
            return with("beforeUserTime", DateUtils.print(date));
        }

        public SearchQueryBuilder afterUserTime(DateTime date) {
            return with("afterUserTime", DateUtils.print(date));
        }

        public List<E> execute() throws IOException {
            GenericUrl url = url();
            url.putAll(params);
            return search(url);
        }

    }

}
