package org.cri.redmetrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by himmelattack on 23/02/15.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ApplicationError {

    private int code;

    private String description;
}
