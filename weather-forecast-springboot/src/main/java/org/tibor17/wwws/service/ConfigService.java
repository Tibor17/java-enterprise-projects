package org.tibor17.wwws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tibor17.wwws.dto.GeoLocation;
import org.tibor17.wwws.repository.LocationRepository;
import org.tibor17.wwws.repository.SettingsRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class ConfigService {
    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private LocationRepository locationRepository;

    public String findConnectionUrl() {
        var rows = settingsRepository.findAll().iterator();
        return rows.hasNext() ? rows.next().getUrl() : null;
    }

    public String findAuthKey() {
        var rows = settingsRepository.findAll().iterator();
        return rows.hasNext() ? rows.next().getAuthKey() : null;
    }

    public Collection<GeoLocation> findAllLocations() {
        var locations = new ArrayList<GeoLocation>();
        locationRepository.findAll()
                .forEach(e -> locations.add(new GeoLocation(e.getLatitude(), e.getLongitude())));
        return locations;
    }
}
