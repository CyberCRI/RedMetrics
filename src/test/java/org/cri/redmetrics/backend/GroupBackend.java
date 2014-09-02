package org.cri.redmetrics.backend;

import org.cri.redmetrics.model.TestGroup;

public class GroupBackend extends HttpBackend<TestGroup> {

    public GroupBackend() {
        super("group/", TestGroup.class);
    }

}
