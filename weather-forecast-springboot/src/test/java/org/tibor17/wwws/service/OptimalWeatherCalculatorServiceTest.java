package org.tibor17.wwws.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.tibor17.wwws.util.OptimalWeatherUtil.*;

public class OptimalWeatherCalculatorServiceTest {

    @Test
    void shouldNotBeSmallWindInRangeOfSpeed() {
        assertThat(isWindSpeedWithinRange(new BigDecimal(4.9d)))
                .isFalse();
    }

    @Test
    void shouldNotBeBigWindInRangeOfSpeed() {
        assertThat(isWindSpeedWithinRange(new BigDecimal(18.1d)))
                .isFalse();
    }

    @Test
    void shouldBeWindInRangeOfSpeed() {
        assertThat(isWindSpeedWithinRange(new BigDecimal(5)))
                .isTrue();

        assertThat(isWindSpeedWithinRange(new BigDecimal(11)))
                .isTrue();

        assertThat(isWindSpeedWithinRange(new BigDecimal(18)))
                .isTrue();
    }

    @Test
    void shouldNotBeLowTemperatureInRange() {
        assertThat(isTemperatureWithinRange(new BigDecimal(4.9d)))
                .isFalse();
    }

    @Test
    void shouldNotBeHighTemperatureInRange() {
        assertThat(isTemperatureWithinRange(new BigDecimal(35.1d)))
                .isFalse();
    }

    @Test
    void shouldBeTemperatureInRange() {
        assertThat(isTemperatureWithinRange(new BigDecimal(5)))
                .isTrue();

        assertThat(isTemperatureWithinRange(new BigDecimal(20)))
                .isTrue();

        assertThat(isTemperatureWithinRange(new BigDecimal(35)))
                .isTrue();
    }

    @Test
    void shouldNotHaveOptimalWeatherConditions1() {
        assertThat(hasOptimalWeatherConditions(new BigDecimal(4.9d), new BigDecimal(20d)))
                .isFalse();
    }

    @Test
    void shouldNotHaveOptimalWeatherConditions2() {
        assertThat(hasOptimalWeatherConditions(new BigDecimal(18.1d), new BigDecimal(20d)))
                .isFalse();
    }

    @Test
    void shouldNotHaveOptimalWeatherConditions3() {
        assertThat(hasOptimalWeatherConditions(new BigDecimal(11), new BigDecimal(4.9d)))
                .isFalse();
    }

    @Test
    void shouldNotHaveOptimalWeatherConditions4() {
        assertThat(hasOptimalWeatherConditions(new BigDecimal(11), new BigDecimal(35.1d)))
                .isFalse();
    }

    @Test
    void shouldNotHaveOptimalWeatherConditions5() {
        assertThat(hasOptimalWeatherConditions(new BigDecimal(4.9d), new BigDecimal(4.9d)))
                .isFalse();
        assertThat(hasOptimalWeatherConditions(new BigDecimal(4.9d), new BigDecimal(35.1d)))
                .isFalse();
        assertThat(hasOptimalWeatherConditions(new BigDecimal(18.1d), new BigDecimal(4.9d)))
                .isFalse();
        assertThat(hasOptimalWeatherConditions(new BigDecimal(18.1d), new BigDecimal(35.1d)))
                .isFalse();

        assertThat(hasOptimalWeatherConditions(new BigDecimal(4.9d), new BigDecimal(4.9d)))
                .isFalse();
        assertThat(hasOptimalWeatherConditions(new BigDecimal(4.9d), new BigDecimal(35.1d)))
                .isFalse();
        assertThat(hasOptimalWeatherConditions(new BigDecimal(18.1d), new BigDecimal(4.9d)))
                .isFalse();
        assertThat(hasOptimalWeatherConditions(new BigDecimal(18.1d), new BigDecimal(35.1d)))
                .isFalse();
    }

    @Test
    void shouldHaveOptimalWeatherConditions() {
        assertThat(hasOptimalWeatherConditions(new BigDecimal(11), new BigDecimal(20)))
                .isTrue();
    }

    @Test
    void shouldBeLowWindsurfingRatingIndex() {
        assertThat(computeWindsurfingRatingIndex(new BigDecimal(5), new BigDecimal(5)))
                .isEqualTo(new BigDecimal(20));
    }

    @Test
    void shouldBeHighWindsurfingRatingIndex() {
        assertThat(computeWindsurfingRatingIndex(new BigDecimal(18), new BigDecimal(35)))
                .isEqualTo(new BigDecimal(89));
    }

    @Test
    void shouldBeIntermediateWindsurfingRatingIndex() {
        assertThat(computeWindsurfingRatingIndex(new BigDecimal(11), new BigDecimal(20)))
                .isEqualTo(new BigDecimal(53));
    }

    @Test
    void shouldMaskShortAuthKey() {
        assertThat(maskAuthKey("0A2B4"))
                .isEqualTo("*****");
    }

    @Test
    void shouldMaskLongAuthKey() {
        assertThat(maskAuthKey("0a23bc67c9"))
                .isEqualTo("0a2****7c9");
    }
}
