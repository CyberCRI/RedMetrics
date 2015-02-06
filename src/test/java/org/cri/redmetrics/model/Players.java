package org.cri.redmetrics.model;

public class Players {

    public static TestPlayer newJohnSnow() {
        return new PlayerBuilder().withNewRandomExternalId().withGender(Gender.MALE).withPostalCode("00001").withCountry("The North").build();
    }

}
