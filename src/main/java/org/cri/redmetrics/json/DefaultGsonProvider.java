package org.cri.redmetrics.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Provider;

public class DefaultGsonProvider implements Provider<Gson> {

    @Override
    public Gson get() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().create();
    }

}
