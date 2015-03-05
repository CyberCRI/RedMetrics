package org.cri.redmetrics.model;

public class PlayerBuilder {

    private String birthDate;
    private String region;
    private String country;
    private Gender gender;
    private String externalId;

    public TestPlayer build() {
        TestPlayer player = new TestPlayer();
        player.setBirthDate(birthDate);
        player.setRegion(region);
        player.setCountry(country);
        player.setGender(gender.name());
        player.setExternalId(externalId);
        return player;
    }

    public PlayerBuilder withBirthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public PlayerBuilder withRegion(String region) {
        this.region = region;
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
