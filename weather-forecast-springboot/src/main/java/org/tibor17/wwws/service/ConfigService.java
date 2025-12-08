package org.tibor17.wwws.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tibor17.wwws.dto.GeoLocation;
import org.tibor17.wwws.repository.LocationRepository;
import org.tibor17.wwws.repository.SettingsRepository;

import java.util.Collection;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.StreamSupport.stream;
import static org.tibor17.wwws.util.OptimalWeatherUtil.maskAuthKey;

@Service
@Slf4j
public class ConfigService {
    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Transactional(readOnly = true)
    @Cacheable(key = " 'remote-connection-url' ", cacheNames = "string-value")
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

    @Transactional(readOnly = true)
    @Cacheable(key = " 'auth-key' ", cacheNames = "string-value")
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

    @Transactional(readOnly = true)
    @Cacheable(key = " 'geos' ", cacheNames = "geo-locations")
    public Collection<GeoLocation> findAllLocations() {
        return stream(locationRepository.findAll().spliterator(), false)
                .map(e -> new GeoLocation(e.getLatitude(), e.getLongitude()))
                .toList();
    }
}
