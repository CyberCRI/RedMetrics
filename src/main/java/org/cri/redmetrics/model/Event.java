package org.cri.redmetrics.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@DatabaseTable(tableName = "events")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Event extends ProgressData {

    @DatabaseField(canBeNull = false)
    private String type;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] sections;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private long[] coordinates;

}
