package org.cri.redmetrics.json;

import org.cri.redmetrics.model.Entity;
import org.cri.redmetrics.util.RouteHelper;
import spark.ResponseTransformer;

import java.util.Collection;

public interface JsonConverter<E extends Entity> extends ResponseTransformer {

    E parse(String json);
    
    Collection<E> parseCollection(String json);

    // Remove the "throws Exception" declaration
    @Override
    public String render(Object model);

    public String render(RouteHelper.DataType dataType, Object model);
}
