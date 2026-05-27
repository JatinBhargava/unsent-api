package com.unsent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.unsent.util.CrudOperation;
import com.unsent.util.RecordStatus;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Column(name = "local_ts", nullable = false)
    private Instant localTs;

    @Column(name = "host_ts", nullable = false)
    private Instant hostTs;

    @Column(name = "crud_value", nullable = false)
    private String crud_value;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @PrePersist
    protected void onCreate() {
        this.localTs = Instant.now();
        this.hostTs = Instant.now();
        // only set if missing
        if (this.crud_value == null) {
            this.crud_value =
                    CrudOperation.CREATE.getCode();
        }
        if (this.status == null) {
            this.status =
                    RecordStatus.ACTIVE.getCode();
        }
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }

    @PreUpdate
    protected void onUpdate() {

        this.localTs = Instant.now();
        this.hostTs = Instant.now();

        if (this.crud_value == null) {
            this.crud_value =
                    CrudOperation.UPDATE.getCode();
        }

        if (this.status == null) {
            this.status =
                    RecordStatus.ACTIVE.getCode();
        }
    }

}
