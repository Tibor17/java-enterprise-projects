package org.tibor17.wwws.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record GeoLocation(@JsonProperty BigDecimal latitude,
                          @JsonProperty BigDecimal longitude) {
}
