package org.tibor17.wwws.repository;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import org.tibor17.wwws.domain.SettingsEntity;

@Repository
public interface SettingsRepository extends CrudRepository<SettingsEntity, Long> {
}
