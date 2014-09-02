package org.cri.redmetrics.util;

public class TestUtils {

    public static int randomId() {
        return (int) Math.round(Math.random() * 1000000000) + 1000000000;
    }

}
