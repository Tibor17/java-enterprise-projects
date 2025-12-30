package org.tibor17.wwws.service;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.tibor17.wwws.dto.GeoLocation;
import org.tibor17.wwws.repository.LocationRepository;
import org.tibor17.wwws.repository.SettingsRepository;
import org.tibor17.wwws.util.DefaultCacheKeyGenerator;

import java.util.Collection;
import java.util.Optional;

import static jakarta.transaction.Transactional.TxType.NOT_SUPPORTED;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.tibor17.wwws.util.OptimalWeatherUtil.maskAuthKey;

@ApplicationScoped
@Slf4j
public class ConfigService {
    @Inject
    SettingsRepository settingsRepository;

    @Inject
    LocationRepository locationRepository;

    @Transactional(NOT_SUPPORTED)
    @CacheResult(cacheName = "string-value", keyGenerator = DefaultCacheKeyGenerator.class)
    public Optional<String> findConnectionUrl() {
        var rows = settingsRepository.findAll().iterator();

        Optional<String> url = empty();
        if (rows.hasNext()) {
            var urlRecord = rows.next().getUrl();
            url = of(urlRecord);
            log.info("Connection URL: {} in settings.", urlRecord);
        }

        if (rows.hasNext()) {
            log.warn("There are more than one row of Connection URL in settings!");
        }

        return url;
    }

    @Transactional(NOT_SUPPORTED)
    @CacheResult(cacheName = "string-value", keyGenerator = DefaultCacheKeyGenerator.class)
    public Optional<String> findAuthKey() {
        var rows = settingsRepository.findAll().iterator();

        Optional<String> authKey = empty();
        if (rows.hasNext()) {
            var authKeyRecord = rows.next().getAuthKey();
            authKey = of(authKeyRecord);
            log.info("AuthKey: {} in settings.", maskAuthKey(authKeyRecord));
        }

        if (rows.hasNext()) {
            log.warn("There are more than one row of AuthKey in settings!");
        }

        return authKey;
    }

    @Transactional(NOT_SUPPORTED)
    @CacheResult(cacheName = "geo-locations", keyGenerator = DefaultCacheKeyGenerator.class)
    public Collection<GeoLocation> findAllLocations() {
        return locationRepository.findAll()
                .map(e -> new GeoLocation(e.getLatitude(), e.getLongitude()))
                .toList();
    }
}
