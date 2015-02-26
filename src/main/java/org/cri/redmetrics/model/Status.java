package org.cri.redmetrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by himmelattack on 24/02/15.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Status {

    private String apiVersion;

    private String build;

    private Date startedAt;
}
