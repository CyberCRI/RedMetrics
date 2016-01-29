package org.cri.redmetrics.util;

import org.cri.redmetrics.csv.CsvResponseTransformer;
import org.cri.redmetrics.json.JsonConverter;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Arrays;
import java.util.List;

import static spark.Spark.*;

/**
 * Created by himmelattack on 23/02/15.
 */
public class RouteHelper {

    public enum HttpVerb {
        GET,
        POST,
        PUT,
        DELETE,
        OPTIONS
    }

    public enum ContentType {
        JSON,
        CSV
    }

    public enum DataType {
        ENTITY,
        ENTITY_OR_ID_LIST,
        ENTITY_LIST_OR_RESULTS_PAGE,
        BIN_COUNT_LIST
    }

    public static ContentType determineContentType(Request request) {
        // If the user requests a format in the URL
        if(request.url().endsWith(".json")) {
            return ContentType.JSON;
        }
        else if(request.url().endsWith(".csv")) {
            return ContentType.CSV;
        }

        // If the user requests a format in the query string
        if(request.queryMap("format").hasValue()) {
            if ("json".equalsIgnoreCase(request.queryMap("format").value())) {
                return ContentType.JSON;
            }
            if ("csv".equalsIgnoreCase(request.queryMap("format").value())) {
                return ContentType.CSV;
            }

            throw new IllegalArgumentException("Incorrect format requested. Only CSV or JSON is recognized");
        }

        // If the user requests a format in the header
        List<String> acceptedContentTypes = Arrays.asList(request.headers("Accept").split(","));
        if(acceptedContentTypes.contains("application/json")) {
            return ContentType.JSON;
        }
        if(acceptedContentTypes.contains("text/csv")) {
            return ContentType.CSV;
        }

        // No format specified, so default to JSON
        return ContentType.JSON;
    }


    protected final JsonConverter jsonConverter;
    protected final CsvResponseTransformer csvResponseTransformer;

    public RouteHelper(JsonConverter jsonConverter, CsvResponseTransformer csvResponseTransformer) {
        this.jsonConverter = jsonConverter;
        this.csvResponseTransformer = csvResponseTransformer;
    }

    public void publishRouteSet(HttpVerb verb, DataType dataType, String path, Route route) {
        // Publish a set of routes with or without a slash and with a provided format
        publishSingleRoute(verb, dataType, path, route);
        publishSingleRoute(verb, dataType, path + "/", route);
        publishSingleRoute(verb, dataType, path + ".json", route);
        publishSingleRoute(verb, dataType, path + ".csv", route);
    }

    public void publishOptionsRouteSet(String path) {
        // Always return empty response with CORS headers
        Route route = (request, response) -> { return ""; };

        options(path, route);
        options(path + "/", route);
        options(path + ".json", route);
        options(path + ".csv", route);
    }


    // A wrapper route to deduce the correct content type
    protected class RouteWrapper implements Route {
        private DataType dataType;
        private Route route;

        public RouteWrapper(DataType dataType, Route route) {
            this.dataType = dataType;
            this.route = route;
        }

        public Object handle(Request request, Response response) {
            ContentType contentType = RouteHelper.determineContentType(request);
            return evaluateAndFormatResponse(contentType, route, request, response);
        }


        private String evaluateAndFormatResponse(ContentType contentType, Route route, Request request, Response response) {
            // Set the content type after the evaluation to not mess up the content type of errors
            String body;
            switch(contentType) {
                case CSV:
                    body = csvResponseTransformer.render(dataType, route.handle(request, response));
                    response.type("text/csv");
                    break;
                case JSON:
                    body = jsonConverter.render(dataType, route.handle(request, response));
                    response.type("application/json");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown content type");
            }
            return body;
        }
    }

    private void publishSingleRoute(HttpVerb verb, DataType dataType, String path, Route route) {
        RouteWrapper routeWrapper = new RouteWrapper(dataType, route);

        switch (verb) {
            case GET:
                get(path, routeWrapper);
                break;
            case POST:
                post(path, routeWrapper);
                break;
            case PUT:
                put(path, routeWrapper);
                break;
            case DELETE:
                delete(path, routeWrapper);
                break;
            default:
                throw new RuntimeException("Unknown verb " + verb);
        }
    }
}
