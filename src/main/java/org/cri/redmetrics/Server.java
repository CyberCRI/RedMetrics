package org.cri.redmetrics;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.cri.redmetrics.controller.*;
import org.cri.redmetrics.dao.DbException;
import org.cri.redmetrics.dao.InconsistentDataException;
import org.cri.redmetrics.db.Db;
import org.cri.redmetrics.guice.MainModule;
import org.cri.redmetrics.json.DefaultGsonProvider;
import org.cri.redmetrics.json.DefaultJsonConverter;
import org.cri.redmetrics.model.ApplicationError;
import org.cri.redmetrics.model.Status;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;
import java.util.Date;

import static spark.Spark.*;

public class Server {

    public static String hostName;

    private static void enableCORS() {
        before((Request request, Response response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type");
            response.header("Access-Control-Expose-Headers", "Content-Type, Location, Link, X-Total-Count, X-Page-Count, X-Per-Page-Count, X-Page-Number");
        });
    }


    private final int portNumber;
    private final Db database;

    private DefaultJsonConverter defaultJsonConverter;
    private Date startDate = new Date();

    private boolean started;

    public Server(int portNumber, Db database) {
        this.portNumber = portNumber;
        this.database = database;

        Gson gson = new DefaultGsonProvider().get();
        this.defaultJsonConverter = new DefaultJsonConverter(gson);
    }
    
    public int getPort(){
        return portNumber;
    }

    public void start() {

        if (started) return;
        started = true;

        setPort(portNumber);

        enableCORS();

        Injector injector = Guice.createInjector(new MainModule(database));

        Class[] controllers = {
                GameController.class,
                GameVersionController.class,
                PlayerController.class,
                EventController.class,
                SnapshotController.class,
                GroupController.class
        };
        for (Class<Controller> controllerClass : (Class<Controller>[]) controllers) {
            Controller controller = injector.getInstance(controllerClass);
            controller.publish();
        }

        // Status route
        Route statusRoute = (request, response) -> {
            Status status = new Status("1", "", startDate);
            response.type("application/json");
            return defaultJsonConverter.render(status);
        };
        get("/status", statusRoute);
        get("/status/", statusRoute);

        // Setup exceptions
        exception(JsonSyntaxException.class, (e, request, response) -> {
            writeError(response, 400, "Malformed JSON : " + e.getCause().getMessage());
        });

        exception(DbException.class, (e, request, response) -> {
            SQLException exception = ((DbException) e).getSqlException();
            Throwable cause = exception.getCause();
            String message;
            if (cause != null) {
                message = cause.getMessage();
            } else {
                message = exception.getMessage();
            }
            e.printStackTrace();

            writeError(response, 400, message);
        });

        exception(NumberFormatException.class, (e, request, response) -> {
            writeError(response, 400, "Unsupported JSON Format : A number was expected " + e.getMessage());
        });

        exception(InconsistentDataException.class, (e, request, response) -> {
            writeError(response, 400, InconsistentDataException.class.getSimpleName() + " : " + e.getMessage());
        });

        exception(IllegalArgumentException.class, (e, request, response) -> {
            writeError(response, 400, e.getMessage());
        });
    }

    private void writeError(Response response, int code, String message) {
        response.status(code);
        
        ApplicationError error = new ApplicationError(code, message);
        response.type("application/json");
        response.body(defaultJsonConverter.render(error));
    }
}
