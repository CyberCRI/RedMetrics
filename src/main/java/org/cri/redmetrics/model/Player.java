package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@DatabaseTable(tableName = "players")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Player extends Entity {

    @DatabaseField
    private Date birthDate;

    @DatabaseField
    private String region;

    @DatabaseField
    private String country;

    @DatabaseField
    private Gender gender;

    @DatabaseField(index = true)
    private String externalId;

}
