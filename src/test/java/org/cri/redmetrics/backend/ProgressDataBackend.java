package org.cri.redmetrics.backend;

import org.cri.redmetrics.model.TestProgressData;

public class ProgressDataBackend<E extends TestProgressData> extends HttpBackend<E> {

    ProgressDataBackend(String entityPath, Class<E> type) {
        super(entityPath, type);
    }

}
