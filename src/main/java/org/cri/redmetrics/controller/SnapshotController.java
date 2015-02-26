package org.cri.redmetrics.controller;

import com.google.inject.Inject;
import org.cri.redmetrics.csv.CsvEntityConverter;
import org.cri.redmetrics.dao.SnapshotDao;
import org.cri.redmetrics.json.SnapshotJsonConverter;
import org.cri.redmetrics.model.Snapshot;

public class SnapshotController extends ProgressDataController<Snapshot, SnapshotDao> {

    @Inject
    SnapshotController(SnapshotDao dao, SnapshotJsonConverter json, CsvEntityConverter<Snapshot> csvEntityConverter) {
        super("snapshot", dao, json, csvEntityConverter);
    }

    @Override
    protected String[] searchableValues() {
        return VALUES;
    }

}
