package org.cri.redmetrics.model;

import java.util.List;

public class ResultsPage<E extends Entity> {
    public ResultsPage(long total, long page, long perPage, List<E> results) {
        this.total = total;
        this.page = page;
        this.perPage = perPage;
        this.results = results;
    }

    public long total;
    public long page;
    public long perPage;
    public List<E> results;
}
