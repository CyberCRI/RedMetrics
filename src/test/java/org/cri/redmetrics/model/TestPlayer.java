package org.cri.redmetrics.model;


import com.google.api.client.util.Key;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestPlayer extends TestEntity {

    @Key
    private String email;

    @Key
    private String firstName;

    @Key
    private String lastName;

    @Key
    private String birthDate;

    @Key
    private TestAddress address;

    @Key
    private String gender;
}
