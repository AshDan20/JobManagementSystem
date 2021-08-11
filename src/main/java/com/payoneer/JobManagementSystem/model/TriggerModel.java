package com.payoneer.JobManagementSystem.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Entity
@Table(name = "trigger")
public class TriggerModel implements Serializable {

    public static final long serialVersionUID = -7399646251619904461L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String triggergroup;

    @NotEmpty
    private String cron;

    public TriggerModel() {

    }

    public Long getId() {
        return id;
    }

    public String getTriggergroup() {
        return triggergroup;
    }

    public void setTriggergroup(String triggergroup) {
        this.triggergroup = triggergroup;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public TriggerModel(Long id, @NotEmpty String name, @NotEmpty String triggergroup, @NotEmpty String cron) {
        this.id = id;
        this.name = name;
        this.triggergroup = triggergroup;
        this.cron = cron;
    }
}
