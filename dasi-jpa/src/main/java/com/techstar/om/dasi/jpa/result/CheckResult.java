package com.techstar.om.dasi.jpa.result;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.techstar.framework.jpa.jackson.LongSerializer;
import com.techstar.om.dasi.domain.EPriority;
import com.techstar.om.dasi.jpa.convert.DateTimeConverter;
import com.techstar.om.dasi.jpa.info.CheckPoint;
import com.techstar.om.dasi.jpa.info.CheckTarget;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "om_check_result")
public class CheckResult implements Persistable<Long> {
    @Id
    @JsonSerialize(using = LongSerializer.class)
    @Column(name = "check_point", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_point", insertable = false, updatable = false, nullable = false)
    private CheckPoint point;

    @Column(name = "check_time")
    @Convert(converter = DateTimeConverter.class)
    private DateTime checkTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_target")
    private CheckTarget target;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "priority")
    private EPriority priority;

    @Column(name = "hint")
    private String hint;

    @Column(name = "return_status")
    private Integer returnStatus;

    @Column(name = "return_data")
    private String returnData;

    public CheckResult() {
    }

    public CheckResult(Long id) {
        this.id = id;
    }

    public CheckResult(CheckPoint point) {
        this.point = point;
        this.id = point.getId();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Transient
    @Override
    public boolean isNew() {
        return false;
    }

    public void setPriority(EPriority priority) {
        if (this.priority == null || priority == null || this.priority.ordinal() < priority.ordinal()) {
            this.priority = priority;
        }
    }

    public void setHint(String hint) {
        if (this.hint == null) {
            this.hint = hint;
        } else if (hint != null) {
            this.hint = this.hint + ";" + hint;
        }
    }

}
