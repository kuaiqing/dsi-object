package com.techstar.om.dasi.jpa.result;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.techstar.framework.jpa.jackson.LongSerializer;
import com.techstar.om.dasi.jpa.convert.DateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class CheckResultBase<T> implements Persistable<T> {
    @JsonSerialize(using = LongSerializer.class)
    @Column(name = "check_target")
    private Long target;
    @Column(name = "check_time")
    @Convert(converter = DateTimeConverter.class)
    private DateTime checkTime;

    public CheckResultBase() {
    }

    public CheckResultBase(CheckResult result) {
        this.target = result.getTarget().getId();
        this.checkTime = result.getCheckTime();
    }

    @Override
    public boolean isNew() {
        return false;
    }

    public abstract void setValue(Double value);
}
