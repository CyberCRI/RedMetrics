package org.cri.redmetrics;


import com.google.api.client.util.Key;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cri.redmetrics.model.Address;
import org.cri.redmetrics.model.Gender;

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
    private Date birthDate;

    @Key
    private Address address;

    @Key
    private Gender gender;

}
