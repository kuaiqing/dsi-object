package com.techstar.om.dasi.jpa.info;

import com.techstar.framework.jpa.common.JpaGeneratedObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "om_check_scheduler_info")
public class CheckScheduler extends JpaGeneratedObject {
    @Column(name = "cron")
    private String cron;

    public CheckScheduler() {
    }

    public CheckScheduler(Long id) {
        super(id);
    }
}
