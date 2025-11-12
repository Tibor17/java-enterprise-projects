package org.tibor17.wwws.repository;

import org.springframework.data.repository.CrudRepository;
import org.tibor17.wwws.domain.SettingsEntity;

public interface SettingsRepository extends CrudRepository<SettingsEntity, Long> {
}
