package org.cri.redmetrics;

import com.google.gson.JsonSyntaxException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.cri.redmetrics.controller.*;
import org.cri.redmetrics.dao.DbException;
import org.cri.redmetrics.dao.InconsistentDataException;
import org.cri.redmetrics.guice.MainModule;

import java.sql.SQLException;

import static spark.Spark.*;

public class Server {

    private final int portNumber;

    private boolean started;

    public Server() {
        this(4567);
    }

    public Server(int portNumber) {
        this.portNumber = portNumber;
    }

    public void start() {

        if (started) return;
        started = true;

        setPort(portNumber);

        Injector injector = Guice.createInjector(new MainModule());

        Class[] controllers = {GameController.class, PlayerController.class, EventController.class, GroupController.class};
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
            e.printStackTrace();
            response.body("Unsupported JSON Format : A number was expected " + e.getMessage());
        });

        exception(InconsistentDataException.class, (e, request, response) -> {
            response.status(400);
            response.body(InconsistentDataException.class.getSimpleName() + " : " + e.getMessage());
        });

    }

}
