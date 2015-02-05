package org.cri.redmetrics.guice;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.j256.ormlite.support.ConnectionSource;
import org.cri.redmetrics.controller.*;
import org.cri.redmetrics.dao.*;
import org.cri.redmetrics.db.Db;
import org.cri.redmetrics.json.*;

import java.sql.SQLException;


public class MainModule extends AbstractModule {
    private final Db database;
    public MainModule(Db database) {
        this.database = database;
    }

    @Override
    protected void configure() {

        bind(Gson.class).toProvider(DefaultGsonProvider.class).asEagerSingleton();
        bind(JsonParser.class).asEagerSingleton();

        bind(GameController.class).asEagerSingleton();
        bind(GameDao.class).asEagerSingleton();
        bind(GameJsonConverter.class).asEagerSingleton();

        bind(Gson.class).annotatedWith(Names.named("GameVersion")).toProvider(GameVersionGsonProvider.class).asEagerSingleton();
        bind(GameVersionController.class).asEagerSingleton();
        bind(GameVersionDao.class).asEagerSingleton();
        bind(GameVersionJsonConverter.class).asEagerSingleton();

        bind(PlayerController.class).asEagerSingleton();
        bind(PlayerDao.class).asEagerSingleton();
        bind(PlayerJsonConverter.class).asEagerSingleton();

        bind(GroupController.class).asEagerSingleton();
        bind(GroupDao.class).asEagerSingleton();
        bind(GroupJsonConverter.class).asEagerSingleton();

        bind(Gson.class).annotatedWith(Names.named("ProgressData")).toProvider(ProgressDataGsonProvider.class).asEagerSingleton();

        bind(EventController.class).asEagerSingleton();
        bind(EventDao.class).asEagerSingleton();
        bind(EventJsonConverter.class).asEagerSingleton();

        bind(SnapshotController.class).asEagerSingleton();
        bind(SnapshotDao.class).asEagerSingleton();
        bind(SnapshotJsonConverter.class).asEagerSingleton();

    }

    @Provides
    @Singleton
    ConnectionSource provideConnectionSource() throws SQLException {
        return database.newConnectionSource();
    }

}
