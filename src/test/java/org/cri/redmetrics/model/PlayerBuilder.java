package org.cri.redmetrics.model;

public class PlayerBuilder {

    private String birthDate;
    private String postalCode;
    private String country;
    private Gender gender;
    private String externalId;

    public TestPlayer build() {
        TestPlayer player = new TestPlayer();
        player.setBirthDate(birthDate);
        player.setPostalCode(postalCode);
        player.setCountry(country);
        player.setGender(gender.name());
        player.setExternalId(externalId);
        return player;
    }

    public PlayerBuilder withBirthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public PlayerBuilder withPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public PlayerBuilder withCountry(String country) {
        this.country = country;
        return this;
    }

    public PlayerBuilder withGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public PlayerBuilder withExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public PlayerBuilder withNewRandomExternalId() {
        return withExternalId(Math.random() + "@test.com");
    }
}
