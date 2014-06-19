package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@DatabaseTable(tableName = "addresses")
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @DatabaseField(id = true)
    private int id;

//    @DatabaseField
//    private String[] lines;

    @DatabaseField
    private String postalCode;

    @DatabaseField
    private String country;

}
