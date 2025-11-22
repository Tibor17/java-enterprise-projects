package org.tibor17.wwws.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tibor17.wwws.dto.GeoLocation;
import org.tibor17.wwws.repository.LocationRepository;
import org.tibor17.wwws.repository.SettingsRepository;

import java.util.Collection;

import static java.util.stream.StreamSupport.stream;
import static org.tibor17.wwws.util.OptimalWeatherUtil.maskAuthKey;

@Service
@Slf4j
public class ConfigService {
    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private LocationRepository locationRepository;

    public String findConnectionUrl() {
        var rows = settingsRepository.findAll().iterator();
        return rows.hasNext() ? rows.next().getUrl() : "";
    }

    @Transactional(readOnly = true)
    public String findAuthKey() {
        var rows = settingsRepository.findAll().iterator();
        var authKey = rows.hasNext() ? rows.next().getAuthKey() : "";
        log.info("AuthKey: {} in settings.", maskAuthKey(authKey));
        if (rows.hasNext()) {
            log.warn("There are more than one rows of AuthKey in settings!");
        }
        return authKey;
    }

    @Transactional(readOnly = true)
    public Collection<GeoLocation> findAllLocations() {
        return stream(locationRepository.findAll().spliterator(), false)
                .map(e -> new GeoLocation(e.getLatitude(), e.getLongitude()))
                .toList();
    }
}
