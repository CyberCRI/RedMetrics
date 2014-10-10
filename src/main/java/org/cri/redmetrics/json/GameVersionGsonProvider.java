package org.cri.redmetrics.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.inject.Provider;

public class GameVersionGsonProvider implements Provider<Gson> {

    @Override
    public Gson get() {
        return DefaultGsonBuilder.get()
                .addDeserializationExclusionStrategy(new GameVersionExclusionStrategy()).create();
    }

    private class GameVersionExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            String fieldName = fieldAttributes.getName();
            return fieldName.equals("game");
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    }
}
