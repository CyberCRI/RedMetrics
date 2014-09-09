package org.cri.redmetrics.backend;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Types;
import org.cri.redmetrics.Server;
import org.cri.redmetrics.controller.Controller;
import org.cri.redmetrics.model.TestEntity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

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

    public List<E> search(String key, String value) throws IOException {
        return (List<E>) requestFactory.buildGetRequest(url(key, value)).execute().parseAs(arrayType);
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

    private GenericUrl url(String relativePath) {
        return new GenericUrl(path + relativePath);
    }

    private GenericUrl url(String key, String value) {
        GenericUrl url = new GenericUrl(path);
        url.put(key, value);
        return url;
    }
}
