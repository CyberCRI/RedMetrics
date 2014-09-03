
package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@DatabaseTable(tableName = "groups")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Group extends Entity{

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private String description;

    @DatabaseField
    private String creator;

    @DatabaseField
    private boolean open;
}
