package com.techstar.om.dasi.jpa.result;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "om_check_result_process_status")
public class CheckResultProcessStatus extends CheckResultBase<CheckResultProcessStatusId> {
    @EmbeddedId
    private CheckResultProcessStatusId id;

    @Column(name = "cpu")
    private Float cpu;
    @Column(name = "mem")
    private Float mem;
    @Column(name = "vsz")
    private Integer vsz;
    @Column(name = "rss")
    private Integer rss;
    @Column(name = "start")
    private String start;
    @Column(name = "time")
    private String time;

    public CheckResultProcessStatus() {
    }

    @Override
    public void setValue(Double value) {

    }

    public CheckResultProcessStatus(CheckResultProcessStatusId id) {
        this.id = id;
    }

    public CheckResultProcessStatus(CheckResultProcessStatusId id, CheckResult result) {
        super(result);
        this.id = id;
    }
}
