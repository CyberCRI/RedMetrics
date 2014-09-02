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
import org.cri.redmetrics.TestEntity;
import org.cri.redmetrics.controller.Controller;

import java.io.IOException;

public class HttpBackend<E extends TestEntity> {

    protected static final HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory((request) -> request.setParser(new JsonObjectParser(new GsonFactory())));
    protected String path;
    protected Class<E> type;

    public HttpBackend(String path, Class<E> type) {
        this.path = path;
        this.type = type;
        Server.start();
    }

    public E get(int id) throws IOException {
        return get(path + id);
    }

    public E post(E entity) throws IOException {
        return post(path, entity);
    }

    public E put(E entity) throws IOException {
        return put(path + entity.getId(), entity);
    }

    public E put(int id, E entity) throws IOException {
        return put(path + id, entity);
    }

    public E delete(int id) throws IOException {
        return delete(path + id);
    }

    public E get(String path) throws IOException {
        return requestFactory.buildGetRequest(url(path)).execute().parseAs(type);
    }

    public HttpContent asJson(E entity) {
        return new JsonHttpContent(new GsonFactory(), entity);
    }

    public HttpResponse buildPostRequest(String path, E entity) throws IOException {
        HttpContent json = asJson(entity);
        return requestFactory.buildPostRequest(url(path), json).execute();
    }

    public E post(String path, E entity) throws IOException {
        return buildPostRequest(path, entity).parseAs(type);
    }

    public E put(String path, E entity) throws IOException {
        HttpContent json = asJson(entity);
        return requestFactory.buildPutRequest(url(path), json).execute().parseAs(type);
    }

    public E delete(String path) throws IOException {
        return requestFactory.buildDeleteRequest(url(path)).execute().parseAs(type);
    }

    private GenericUrl url(String path) {
        return new GenericUrl("http://localhost:4567/" + Controller.basePath + path);
    }

}
