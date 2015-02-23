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
    };

    protected final JsonConverter jsonConverter;
    protected final CsvResponseTransformer csvResponseTransformer;

    public RouteHelper(JsonConverter jsonConverter, CsvResponseTransformer csvResponseTransformer) {
        this.jsonConverter = jsonConverter;
        this.csvResponseTransformer = csvResponseTransformer;
    }

    public void publishRouteSet(HttpVerb verb, String path, Route route) {
        // Publish a set of routes with or without a slash and with a provided format
        publishSingleRoute(verb, path, route);
        publishSingleRoute(verb, path + "/", route);
        publishSingleRoute(verb, path + ".json", route);
        publishSingleRoute(verb, path + ".csv", route);
    }


    protected enum ContentType {
        JSON,
        CSV
    };

    // A wrapper route to deduce the correct content type
    protected class RouteWrapper implements Route {
        private Route route;

        public RouteWrapper(Route route) {
            this.route = route;
        }

        public Object handle(Request request, Response response) {
            // If the user requests a format in the URL
            if(request.url().endsWith(".json")) {
                return evaluateAndFormatResponse(ContentType.JSON, route, request, response);
            }
            else if(request.url().endsWith(".csv")) {
                return evaluateAndFormatResponse(ContentType.CSV, route, request, response);
            }

            // If the user requests a format in the query string
            if(request.queryMap("format").hasValue()) {
                if ("json".equalsIgnoreCase(request.queryMap("format").value())) {
                    return evaluateAndFormatResponse(ContentType.JSON, route, request, response);
                }
                if ("csv".equalsIgnoreCase(request.queryMap("format").value())) {
                    return evaluateAndFormatResponse(ContentType.CSV, route, request, response);
                }

                halt(400, "Incorrect format requested. Only CSV or JSON is recognized.");
            }

            // If the user requests a format in the header
            List<String> acceptedContentTypes = Arrays.asList(request.headers("Accept").split(","));
            if(acceptedContentTypes.contains("application/json")) {
                return evaluateAndFormatResponse(ContentType.JSON, route, request, response);
            }
            if(acceptedContentTypes.contains("text/csv")) {
                return evaluateAndFormatResponse(ContentType.CSV, route, request, response);
            }

            // No format specified, so default to JSON
            return evaluateAndFormatResponse(ContentType.JSON, route, request, response);
        };

        private String evaluateAndFormatResponse(ContentType contentType, Route route, Request request, Response response) {
            switch(contentType) {
                case CSV:
                    response.type("text/csv");
                    return csvResponseTransformer.render(route.handle(request, response));
                case JSON:
                    response.type("application/json");
                    return jsonConverter.render(route.handle(request, response));
                default:
                    throw new IllegalArgumentException("Unknown content type");
            }
        }
    }

    private void publishSingleRoute(HttpVerb verb, String path, Route route) {
        RouteWrapper routeWrapper = new RouteWrapper(route);

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
            case OPTIONS:
                options(path, routeWrapper);
                break;
            default:
                throw new RuntimeException("Unknown verb " + verb);
        }
    }
}
