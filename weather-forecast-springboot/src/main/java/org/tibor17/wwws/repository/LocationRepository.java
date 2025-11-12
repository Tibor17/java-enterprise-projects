package org.tibor17.wwws.repository;

import org.springframework.data.repository.CrudRepository;
import org.tibor17.wwws.domain.LocationEntity;

public interface LocationRepository extends CrudRepository<LocationEntity, Long> {
}
