
package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DatabaseTable(tableName = "groups")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Group implements Entity{
    
    @DatabaseField(generatedId = true)
    private int id;
    
    @DatabaseField(canBeNull = false)
    private String name;
    
    @DatabaseField
    private String description;
    
    @DatabaseField
    private String creator;
    
    @DatabaseField
    private boolean open;
}
