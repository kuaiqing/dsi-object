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
public class CheckResultDiskFreeId implements Serializable {
    @Column(name = "applied_time", nullable = false)
    @Convert(converter = DateTimeConverter.class)
    private DateTime appliedTime;
    @JsonSerialize(using = LongSerializer.class)
    @Column(name = "check_point", nullable = false)
    private long point;
    @Column(name = "mount", nullable = false)
    private String mount;

    public CheckResultDiskFreeId() {
    }

    public CheckResultDiskFreeId(DateTime appliedTime, long point, String mount) {
        this.appliedTime = appliedTime;
        this.point = point;
        this.mount = mount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckResultDiskFreeId that = (CheckResultDiskFreeId) o;
        return point == that.point &&
                appliedTime.equals(that.appliedTime) &&
                mount.equals(that.mount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appliedTime, point, mount);
    }
}
