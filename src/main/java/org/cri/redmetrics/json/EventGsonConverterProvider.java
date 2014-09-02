package org.cri.redmetrics.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.inject.Provider;

public class EventGsonConverterProvider implements Provider<Gson> {

    @Override
    public Gson get() {
        return DefaultGsonBuilder.get()
                .addDeserializationExclusionStrategy(new DataExclusionStrategy()).create();
    }

    private class DataExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            String fieldName = fieldAttributes.getName();
            if (fieldName == "customData" || fieldName == "game" || fieldName == "player")
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
