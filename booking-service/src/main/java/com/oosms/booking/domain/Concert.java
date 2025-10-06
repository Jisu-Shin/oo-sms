package com.oosms.booking.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("C")
@Getter
@Setter
public class Concert extends Item {
}
