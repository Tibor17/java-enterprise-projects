package org.tibor17.wwws.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "settings")
@Setter @Getter
public class SettingsEntity extends BaseEntity {

    @Column
    private String url;

    @Column(name = "authkey")
    private String authKey;
}
