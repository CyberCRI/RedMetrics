package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@DatabaseTable(tableName = "snapshots")
@EqualsAndHashCode(callSuper = true)
public class Snapshot extends ProgressData {

    @DatabaseField
    private Date gameTime;

}
