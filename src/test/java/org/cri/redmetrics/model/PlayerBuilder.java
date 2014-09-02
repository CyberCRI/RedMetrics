package org.cri.redmetrics.model;

public class PlayerBuilder {

    private String firstName;
    private String lastName;
    private String birthDate;
    private TestAddress address;
    private Gender gender;

    public TestPlayer build() {
        TestPlayer player = new TestPlayer();
        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setBirthDate(birthDate);
        player.setAddress(address);
        player.setGender(gender.name());
        return player;
    }

    public PlayerBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public PlayerBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public PlayerBuilder withBirthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public PlayerBuilder withAddress(String postalCode, String country) {
        this.address = new TestAddress();
        this.address.setPostalCode(postalCode);
        this.address.setCountry(country);
        return this;
    }

    public PlayerBuilder withGender(Gender gender) {
        this.gender = gender;
        return this;
    }

}
