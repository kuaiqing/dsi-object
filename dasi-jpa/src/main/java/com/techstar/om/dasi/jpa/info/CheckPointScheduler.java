package com.techstar.om.dasi.jpa.info;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.techstar.framework.jpa.jackson.ObjectSerializer;
import com.techstar.om.dasi.jpa.convert.DateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "om_check_point_scheduler")
public class CheckPointScheduler implements Persistable<CheckPointSchedulerId> {
    @EmbeddedId
    private CheckPointSchedulerId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_point", nullable = false, insertable = false, updatable = false)
    private CheckPoint point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_scheduler", nullable = false, insertable = false, updatable = false)
    @JsonSerialize(using = ObjectSerializer.IdName.class)
    private CheckScheduler scheduler;

    @Column(name = "last_run_time", insertable = false, updatable = false)
    @Convert(converter = DateTimeConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private DateTime lastRunTime;
    @Column(name = "last_complete_time", insertable = false, updatable = false)
    @Convert(converter = DateTimeConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private DateTime lastCompleteTime;
    @Column(name = "next_scheduler_time", insertable = false, updatable = false)
    @Convert(converter = DateTimeConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private DateTime nextSchedulerTime;

    @Override
    public CheckPointSchedulerId getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
