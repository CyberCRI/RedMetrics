package org.cri.redmetrics.guice;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.cri.redmetrics.controller.EventController;
import org.cri.redmetrics.controller.GameController;
import org.cri.redmetrics.controller.PlayerController;
import org.cri.redmetrics.dao.EventDao;
import org.cri.redmetrics.dao.GameDao;
import org.cri.redmetrics.dao.PlayerDao;
import org.cri.redmetrics.json.*;

import java.sql.SQLException;

public class MainModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(Gson.class).toProvider(DefaultGsonProvider.class).asEagerSingleton();
        bind(Gson.class).annotatedWith(Names.named("Event")).toProvider(EventGsonConverterProvider.class).asEagerSingleton();
        bind(JsonParser.class).asEagerSingleton();
        bind(GameJsonConverter.class).asEagerSingleton();
        bind(PlayerJsonConverter.class).asEagerSingleton();
        bind(EventJsonConverter.class).asEagerSingleton();

        bind(GameController.class).asEagerSingleton();
        bind(GameDao.class).asEagerSingleton();

        bind(PlayerController.class).asEagerSingleton();
        bind(PlayerDao.class).asEagerSingleton();

        bind(EventController.class).asEagerSingleton();
        bind(EventDao.class).asEagerSingleton();

        bind(JsonParser.class).asEagerSingleton();

    }

    @Provides
    @Singleton
    ConnectionSource provideConnectionSource() throws SQLException {
        String databaseUrl = "jdbc:postgresql://localhost:5432/test";
        return new JdbcConnectionSource(databaseUrl, "postgres", "admin");
    }

}
