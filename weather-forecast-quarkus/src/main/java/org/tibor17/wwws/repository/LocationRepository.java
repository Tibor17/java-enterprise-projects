package org.tibor17.wwws.repository;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import org.tibor17.wwws.domain.LocationEntity;

@Repository
public interface LocationRepository extends CrudRepository<LocationEntity, Long> {
}
