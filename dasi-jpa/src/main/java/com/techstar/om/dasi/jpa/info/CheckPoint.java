package com.techstar.om.dasi.jpa.info;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.techstar.framework.jpa.common.JpaGeneratedObject;
import com.techstar.framework.jpa.jackson.ObjectSerializer;
import com.techstar.framework.jpa.jackson.StringSerializer;
import com.techstar.om.dasi.jpa.convert.DateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "om_check_point_info")
public class CheckPoint extends JpaGeneratedObject {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_target", nullable = false)
    @JsonSerialize(using = ObjectSerializer.IdName.class)
    private CheckTarget target;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_handler", nullable = false)
    @JsonSerialize(using = StringSerializer.IdName.class)
    private CheckHandler handler;
    @Column(name = "executor_params")
    private String executorParams;
    @Column(name = "executor_timeout")
    private Integer executorTimeout; // 单位秒
    @Column(name = "applied_period")
    private Integer appliedPeriod; // 单位小时
    @Column(name = "disable")
    private Boolean disable;
    @Column(name = "update_time")
    @Convert(converter = DateTimeConverter.class)
    private DateTime updateTime;

    public CheckPoint() {
        updateTime = DateTime.now();
    }

    public CheckPoint(Long id) {
        super(id);
        updateTime = DateTime.now();
    }

    public Integer getExecutorTimeout() {
        return Optional.ofNullable(executorTimeout).orElse(30);
    }

    public Integer getAppliedPeriod() {
        return Optional.ofNullable(appliedPeriod).orElse(1);
    }
}
