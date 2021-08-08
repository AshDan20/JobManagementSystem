package com.payoneer.JobManagementSystem.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Ashish Dandekar
 */

@Entity
@Data
@Table(name = "job")
@EntityListeners(AuditingEntityListener.class)
public class JobModel implements Serializable {

    private static final long serialVersionUID = -4472577046560982567L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String jobname;

    @NotEmpty
    private String jobgroup;

    @OneToOne
    private TriggerModel trigger;

    @ColumnDefault("'QUEUED'")
    private String status;

    @CreatedDate
    @Column
    private LocalDateTime created;

    @LastModifiedDate
    @Column
    private LocalDateTime modified;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public TriggerModel getTrigger() {
        return trigger;
    }

    public void setTrigger(TriggerModel trigger) {
        this.trigger = trigger;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJobgroup() {
        return jobgroup;
    }

    public void setJobgroup(String jobgroup) {
        this.jobgroup = jobgroup;
    }
}
