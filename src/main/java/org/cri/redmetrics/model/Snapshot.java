package org.cri.redmetrics.model;

import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@DatabaseTable(tableName = "snapshots")
@EqualsAndHashCode(callSuper = true)
public class Snapshot extends ProgressData {

}
