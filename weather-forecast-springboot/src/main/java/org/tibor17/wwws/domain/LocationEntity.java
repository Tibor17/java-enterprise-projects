package org.tibor17.wwws.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "geo_location")
@Setter @Getter
public class LocationEntity extends BaseEntity {
    @Column
    @NotNull
    private BigDecimal latitude;

    @Column
    @NotNull
    private BigDecimal longitude;

    @Column
    @NotNull
    private String country;

    @Column
    @NotNull
    private String city;
}
