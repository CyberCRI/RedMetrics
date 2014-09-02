package org.cri.redmetrics.json;

import com.google.gson.GsonBuilder;

class DefaultGsonBuilder {

    static GsonBuilder get() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss");
    }

}
