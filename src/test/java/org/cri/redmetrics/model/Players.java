package org.cri.redmetrics.model;

public class Players {

    public static TestPlayer newJohnSnow() {
        return new PlayerBuilder().withNewRandomExternalId().withGender(Gender.MALE).withRegion("South").withCountry("The North").build();
    }

}
