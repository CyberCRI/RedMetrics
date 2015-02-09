package org.cri.redmetrics;

import com.google.gson.JsonSyntaxException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.cri.redmetrics.controller.*;
import org.cri.redmetrics.dao.DbException;
import org.cri.redmetrics.dao.InconsistentDataException;
import org.cri.redmetrics.guice.MainModule;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import org.cri.redmetrics.db.Db;

import static spark.Spark.*;

public class Server {

    public static String hostName;

    private static void enableCORS() {
        before((Request request, Response response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type");
            response.header("Access-Control-Expose-Headers", "Content-Type, Location, Link, X-Total-Count");
        });
    }


    private final int portNumber;
    private final Db database;

    private boolean started;

    public Server(int portNumber, Db database) {
        this.portNumber = portNumber;
        this.database = database;
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

        after((request, response) -> response.type("application/json"));

        exception(JsonSyntaxException.class, (e, request, response) -> {
            response.status(400);
            response.body("Malformed JSON : " + e.getCause().getMessage());
        });

        exception(DbException.class, (e, request, response) -> {
            response.status(500);
            SQLException exception = ((DbException) e).getSqlException();
            Throwable cause = exception.getCause();
            String message;
            if (cause != null) {
                message = cause.getMessage();
            } else {
                message = exception.getMessage();
            }
            e.printStackTrace();
            response.body("Server Error : " + message);
        });

        exception(NumberFormatException.class, (e, request, response) -> {
            response.status(400);
            response.body("Unsupported JSON Format : A number was expected " + e.getMessage());
        });

        exception(InconsistentDataException.class, (e, request, response) -> {
            response.status(400);
            response.body(InconsistentDataException.class.getSimpleName() + " : " + e.getMessage());
        });

        exception(IllegalArgumentException.class, (e, request, response) -> {
            response.status(400);
            response.body(e.getMessage());
        });
    }

}
