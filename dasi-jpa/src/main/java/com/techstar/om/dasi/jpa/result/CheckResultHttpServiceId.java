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
public class CheckResultHttpServiceId implements Serializable {
    @Column(name = "applied_time", nullable = false)
    @Convert(converter = DateTimeConverter.class)
    private DateTime appliedTime;
    @JsonSerialize(using = LongSerializer.class)
    @Column(name = "check_point", nullable = false)
    private long point;

    public CheckResultHttpServiceId() {
    }

    public CheckResultHttpServiceId(DateTime appliedTime, long point) {
        this.appliedTime = appliedTime;
        this.point = point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CheckResultHttpServiceId that = (CheckResultHttpServiceId) o;
        return point == that.point &&
                appliedTime.equals(that.appliedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), appliedTime, point);
    }
}
