package org.cri.redmetrics.json;

import java.util.Collection;
import java.util.List;
import org.cri.redmetrics.model.Entity;
import spark.ResponseTransformer;

public interface JsonConverter<E extends Entity> extends ResponseTransformer {

    E parse(String json);
    
    Collection<E> parseCollection(String json);

    // Remove the "throws Exception" declaration
    @Override
    public String render(Object model);
}
