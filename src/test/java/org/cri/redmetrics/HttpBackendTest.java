package org.cri.redmetrics;


import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class HttpBackendTest<E extends TestEntity> {

    Server server = new Server();

    HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory((request) -> request.setParser(new JsonObjectParser(new GsonFactory())));

    String path;
    Class<E> type;

    HttpBackendTest(String path, Class<E> type) {
        this.path = path;
        this.type = type;
    }

    @BeforeClass
    public void setUp() throws IOException {
        server.start();
        init();
    }

    abstract void init() throws IOException;

    @AfterClass
    public void tearDown() {
        server.clearAllRoutes();
    }

    E get(int id) throws IOException {
        return get(path + id);
    }

    E post(E entity) throws IOException {
        return post(path, entity);
    }

    E put(E entity) throws IOException {
        return put(path + entity.getId(), entity);
    }

    E delete(int id) throws IOException {
        return delete(path + id);
    }

    E get(String path) throws IOException {
        return requestFactory.buildGetRequest(url(path)).execute().parseAs(type);
    }

    HttpContent asJson(E entity) {
        return new JsonHttpContent(new GsonFactory(), entity);
    }

    HttpResponse buildPostRequest(String path, E entity) throws IOException {
        HttpContent json = asJson(entity);
        return requestFactory.buildPostRequest(url(path), json).execute();
    }

    HttpResponse buildPostRequest(E entity) throws IOException {
        return buildPostRequest(path, entity);
    }

    E post(String path, E entity) throws IOException {
        return buildPostRequest(path, entity).parseAs(type);
    }

    E put(String path, E entity) throws IOException {
        HttpContent json = asJson(entity);
        return requestFactory.buildPutRequest(url(path), json).execute().parseAs(type);
    }

    E delete(String path) throws IOException {
        return requestFactory.buildDeleteRequest(url(path)).execute().parseAs(type);
    }

    int randomId() {
        return (int) Math.round(Math.random() * 1000000000) + 1000000000;
    }

    GenericUrl url(String path) {
        return new GenericUrl("http://localhost:4567/" + path);
    }

}
