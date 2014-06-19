package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@DatabaseTable(tableName = "players")
@NoArgsConstructor
@AllArgsConstructor
public class Player implements Entity {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(index = true, unique = true)
    private String email;

    @DatabaseField
    private String firstName;

    @DatabaseField
    private String lastName;

    @DatabaseField
    private Date birthDate;

    @DatabaseField(foreign = true)
    private Address address;

    @DatabaseField
    private Gender gender;

}
