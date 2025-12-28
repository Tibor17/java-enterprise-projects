package org.tibor17.wwws.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.tibor17.wwws.dto.GeoLocation;
import org.tibor17.wwws.repository.LocationRepository;
import org.tibor17.wwws.repository.SettingsRepository;

import java.util.Collection;

import static jakarta.transaction.Transactional.TxType.NOT_SUPPORTED;
import static org.tibor17.wwws.util.OptimalWeatherUtil.maskAuthKey;

@ApplicationScoped
@Slf4j
public class ConfigService {
    @Inject
    SettingsRepository settingsRepository;

    @Inject
    LocationRepository locationRepository;

    public String findConnectionUrl() {
        var rows = settingsRepository.findAll().iterator();
        return rows.hasNext() ? rows.next().getUrl() : null;
    }

    @Transactional(NOT_SUPPORTED)
    public String findAuthKey() {
        var rows = settingsRepository.findAll().iterator();
        var authKey = rows.hasNext() ? rows.next().getAuthKey() : "";
        log.info("AuthKey: {} in settings.", maskAuthKey(authKey));
        if (rows.hasNext()) {
            log.warn("There are more than one rows of AuthKey in settings!");
        }
        return authKey;
    }

    @Transactional(NOT_SUPPORTED)
    public Collection<GeoLocation> findAllLocations() {
        return locationRepository.findAll()
                .map(e -> new GeoLocation(e.getLatitude(), e.getLongitude()))
                .toList();
    }
}
