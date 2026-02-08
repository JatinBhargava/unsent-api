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

    @Id
    @SequenceGenerator(name = "record_seq", sequenceName = "record_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq")
    @Column(name = "record_id", nullable = false, updatable = false)
    private Long record_id;

    @Column(name = "local_ts", nullable = false)
    private Instant local_ts;

    @Column(name = "host_ts", nullable = false)
    private Instant host_ts;

    @Column(name = "crud_value", nullable = false)
    private String crud_value;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @PrePersist
    protected void onCreate(){
        this.local_ts = Instant.now();
        this.host_ts = Instant.now();
        this.crud_value = CrudOperation.CREATE.getCode();
        this.status = RecordStatus.ACTIVE.getCode();
        this.uuid = UUID.randomUUID();
    }

    @PreUpdate
    protected void onUpdate(){
        this.local_ts = Instant.now();
        this.host_ts = Instant.now();
        this.crud_value = CrudOperation.UPDATE.getCode();
        this.status = RecordStatus.ACTIVE.getCode();
        this.uuid = UUID.randomUUID();
    }

}
