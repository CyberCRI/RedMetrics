package org.cri.redmetrics.model;

import lombok.*;

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
