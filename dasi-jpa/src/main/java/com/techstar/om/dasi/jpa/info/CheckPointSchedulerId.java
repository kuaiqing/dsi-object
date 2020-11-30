package com.techstar.om.dasi.jpa.info;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.techstar.framework.jpa.jackson.LongDeserializer;
import com.techstar.framework.jpa.jackson.LongSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class CheckPointSchedulerId implements Serializable {
    @JsonSerialize(using = LongSerializer.class)
    @JsonDeserialize(using = LongDeserializer.class)
    @Column(name = "check_point", nullable = false)
    private long point;
    @JsonSerialize(using = LongSerializer.class)
    @JsonDeserialize(using = LongDeserializer.class)
    @Column(name = "check_scheduler", nullable = false)
    private long scheduler;

    public CheckPointSchedulerId() {
    }

    public CheckPointSchedulerId(long point, long scheduler) {
        this.point = point;
        this.scheduler = scheduler;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckPointSchedulerId that = (CheckPointSchedulerId) o;
        return point == that.point &&
                scheduler == that.scheduler;
    }

    @Override
    public int hashCode() {
        return Objects.hash(point, scheduler);
    }
}
