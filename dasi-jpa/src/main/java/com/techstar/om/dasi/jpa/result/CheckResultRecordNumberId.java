package com.techstar.om.dasi.jpa.result;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.techstar.framework.jpa.jackson.LongSerializer;
import com.techstar.om.dasi.jpa.convert.DateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class CheckResultRecordNumberId implements Serializable {
    @Column(name = "applied_time", nullable = false)
    @Convert(converter = DateTimeConverter.class)
    private DateTime appliedTime;
    @JsonSerialize(using = LongSerializer.class)
    @Column(name = "check_point", nullable = false)
    private long point;
    @Column(name = "object", nullable = false)
    private String object;
    @Column(name = "metric", nullable = false)
    private String metric;

    public CheckResultRecordNumberId() {
    }

    public CheckResultRecordNumberId(DateTime appliedTime, long point, String object, String metric) {
        this.appliedTime = appliedTime;
        this.point = point;
        this.object = object;
        this.metric = metric;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CheckResultRecordNumberId that = (CheckResultRecordNumberId) o;
        return object.equals(that.object) &&
                metric.equals(that.metric);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), object, metric);
    }
}
