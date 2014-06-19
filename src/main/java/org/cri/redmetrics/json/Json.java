package org.cri.redmetrics.json;

import org.cri.redmetrics.model.Entity;
import spark.ResponseTransformer;

public interface Json<E extends Entity> extends ResponseTransformer {

    E parse(String json);

    String stringify(E entity);

}
