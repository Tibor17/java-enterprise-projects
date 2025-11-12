package org.tibor17.wwws.util;

import java.math.BigDecimal;

public final class OptimalWeatherUtil {
    private static final BigDecimal LOW_WIND_SPEED = new BigDecimal(5);
    private static final BigDecimal HIGH_WIND_SPEED = new BigDecimal(18);
    private static final BigDecimal LOW_TEMPERATURE = new BigDecimal(5);
    private static final BigDecimal HIGH_TEMPERATURE = new BigDecimal(35);
    private static final BigDecimal THREE = new BigDecimal(3);

    private OptimalWeatherUtil() {
        throw new InstantiationError();
    }

    public static boolean isWindSpeedWithinRange(BigDecimal windSpeed) {
        return windSpeed.compareTo(LOW_WIND_SPEED) >= 0
                && windSpeed.compareTo(HIGH_WIND_SPEED) <= 0;
    }

    public static boolean isTemperatureWithinRange(BigDecimal temperature) {
        return temperature.compareTo(LOW_TEMPERATURE) >= 0
                && temperature.compareTo(HIGH_TEMPERATURE) <= 0;
    }

    public static boolean hasOptimalWeatherConditions(BigDecimal windSpeed, BigDecimal temperature) {
        return isWindSpeedWithinRange(windSpeed)
                && isTemperatureWithinRange(temperature);
    }

    public static BigDecimal computeWindsurfingRatingIndex(BigDecimal windSpeed, BigDecimal temperature) {
        return windSpeed.multiply(THREE).add(temperature);
    }
}
