package org.cri.redmetrics.model;

public class Players {

    public static TestPlayer johnSnow() {
        return new PlayerBuilder().withFirstName("John").withLastName("Snow").withGender(Gender.MALE).withAddress("00001", "The North").build();
    }

}
