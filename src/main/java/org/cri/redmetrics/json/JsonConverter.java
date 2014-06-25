package org.cri.redmetrics.json;

import org.cri.redmetrics.model.Entity;
import spark.ResponseTransformer;

public interface JsonConverter<E extends Entity> extends ResponseTransformer {

    E parse(String json);

}
