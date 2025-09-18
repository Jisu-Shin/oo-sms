package com.oosms.sms.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SmsTmpltVarRelId implements Serializable {

    private Long smsTmpltId;
    private Long tmpltVarId;

}
