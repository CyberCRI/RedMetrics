package org.cri.redmetrics.backend;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import org.cri.redmetrics.Server;
import org.cri.redmetrics.controller.Controller;
import org.cri.redmetrics.model.TestEntity;

import java.io.IOException;

public class HttpBackend<E extends TestEntity> {

    private static final int PORT_NUMBER = 7654;
    private static final Server server = new Server(PORT_NUMBER);
    private static final HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory((request) -> request.setParser(new JsonObjectParser(new GsonFactory())));

    private final String path;
    protected Class<E> type;

    public HttpBackend(String entityPath, Class<E> type) {
        this.path = "http://localhost:" + PORT_NUMBER + Controller.basePath + entityPath;
        this.type = type;
        server.start();
    }

    public E get(String id) throws IOException {
        return requestFactory.buildGetRequest(url(id)).execute().parseAs(type);
    }

    public E post(E entity) throws IOException {
        return buildPostRequest(entity).parseAs(type);
    }

    public E put(E entity) throws IOException {
        return put(entity.getId(), entity);
    }

    public E put(String id, E entity) throws IOException {
        HttpContent json = asJson(entity);
        return requestFactory.buildPutRequest(url(id), json).execute().parseAs(type);
    }

    public E delete(String id) throws IOException {
        return requestFactory.buildDeleteRequest(url(id)).execute().parseAs(type);
    }

    private HttpContent asJson(E entity) {
        return new JsonHttpContent(new GsonFactory(), entity);
    }

    private HttpResponse buildPostRequest(E entity) throws IOException {
        HttpContent json = asJson(entity);
        return requestFactory.buildPostRequest(url(), json).execute();
    }

    private GenericUrl url() {
        return new GenericUrl(path);
    }

    private GenericUrl url(String relativePath) {
        return new GenericUrl(path + relativePath);
    }
}
