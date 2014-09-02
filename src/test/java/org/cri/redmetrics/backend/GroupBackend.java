package org.cri.redmetrics.backend;

import org.cri.redmetrics.TestGroup;

public class GroupBackend extends HttpBackend<TestGroup> {

    public GroupBackend() {
        super("group/", TestGroup.class);
    }

}
