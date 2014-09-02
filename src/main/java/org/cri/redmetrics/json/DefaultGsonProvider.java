package org.cri.redmetrics.json;

import com.google.gson.Gson;
import com.google.inject.Provider;

public class DefaultGsonProvider implements Provider<Gson> {

    @Override
    public Gson get() {
        return DefaultGsonBuilder.get().create();
    }

}
