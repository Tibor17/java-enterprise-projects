package org.tibor17.wwws.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;

import static jakarta.persistence.GenerationType.SEQUENCE;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @Id
    @SequenceGenerator(name = "id_seq", sequenceName = "id_seq", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "id_seq")
    private Long id;
}
