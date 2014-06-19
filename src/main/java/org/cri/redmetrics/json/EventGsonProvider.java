package org.cri.redmetrics.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Provider;


public class EventGsonProvider implements Provider<Gson> {

    @Override
    public Gson get() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .addDeserializationExclusionStrategy(new DataExclusionStrategy()).create();
    }

    private class DataExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            String fieldName = fieldAttributes.getName();
            if (fieldName == "data" || fieldName == "game" || fieldName == "player")
                return true;
            else
                return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    }
}
