package org.cri.redmetrics;

import com.google.gson.JsonSyntaxException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.cri.redmetrics.controller.Controller;
import org.cri.redmetrics.controller.EventController;
import org.cri.redmetrics.controller.GameController;
import org.cri.redmetrics.controller.PlayerController;
import org.cri.redmetrics.dao.DbException;
import org.cri.redmetrics.guice.MainModule;

import java.sql.SQLException;

import static spark.Spark.exception;

public class Main {

    public static void main(String[] args) throws SQLException {

        Injector injector = Guice.createInjector(new MainModule());

        Class[] controllers = {GameController.class, PlayerController.class, EventController.class};
        for (Class<Controller> controllerClass : (Class<Controller>[]) controllers) {
            Controller controller = injector.getInstance(controllerClass);
            controller.publish();
        }

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

    }

}
