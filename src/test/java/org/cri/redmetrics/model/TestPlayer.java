package org.cri.redmetrics.model;


import com.google.api.client.util.Key;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TestPlayer extends TestEntity {

    @Key
    private String birthDate;

    @Key
    private String region;

    @Key
    private String country;

    @Key
    private String gender;

    @Key
    private String externalId;
}
