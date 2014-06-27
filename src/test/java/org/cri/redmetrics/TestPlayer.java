package org.cri.redmetrics;


import com.google.api.client.util.Key;
import lombok.Data;
import org.cri.redmetrics.model.Address;
import org.cri.redmetrics.model.Gender;

import java.util.Date;

@Data
public class TestPlayer extends TestEntity {

    @Key
    private String email;

    @Key
    private String firstName;

    @Key
    private String lastName;

    @Key
    private Date birthDate;

    @Key
    private Address address;

    @Key
    private Gender gender;

}
