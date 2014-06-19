package org.cri.redmetrics.json;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.cri.redmetrics.model.Entity;

@RequiredArgsConstructor
abstract class EntityJson<E extends Entity> implements Json<E> {

    protected final Class<E> entityType;
    protected final Gson gson;
    protected final JsonParser jsonParser;

    @Override
    public E parse(String json) {
        return gson.fromJson(json, entityType);
    }

    @Override
    public String stringify(E entity) {
        return gson.toJson(entity);
    }

    @Override
    public final String render(Object model) {
        return stringify((E) model);
    }

}
