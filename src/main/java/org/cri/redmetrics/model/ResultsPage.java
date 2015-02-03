package org.cri.redmetrics.model;

import java.util.List;

public class ResultsPage<E extends Entity> {
    public ResultsPage(long total, long start, long count, List<E> results) {
        this.total = total;
        this.start = start;
        this.count = count;
        this.results = results;
    }

    public long total;
    public long start;
    public long count;
    public List<E> results;
}
