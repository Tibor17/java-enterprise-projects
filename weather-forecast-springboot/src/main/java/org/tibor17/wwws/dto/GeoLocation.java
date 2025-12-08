package org.tibor17.wwws.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record GeoLocation(BigDecimal latitude, BigDecimal longitude) implements Serializable {
}
