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
public class CheckResultProcessStatusId implements Serializable {
    @Column(name = "applied_time", nullable = false)
    @Convert(converter = DateTimeConverter.class)
    private DateTime appliedTime;
    @JsonSerialize(using = LongSerializer.class)
    @Column(name = "check_point", nullable = false)
    private long point;
    @Column(name = "command", nullable = false)
    private String command;
    @Column(name = "user_", nullable = false)
    private String user;
    @Column(name = "pid", nullable = false)
    private int pid;

    public CheckResultProcessStatusId() {
    }

    public CheckResultProcessStatusId(DateTime appliedTime, long point, String command, String user, int pid) {
        this.appliedTime = appliedTime;
        this.point = point;
        this.command = command;
        this.user = user;
        this.pid = pid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckResultProcessStatusId that = (CheckResultProcessStatusId) o;
        return point == that.point &&
                pid == that.pid &&
                appliedTime.equals(that.appliedTime) &&
                command.equals(that.command) &&
                user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appliedTime, point, command, user, pid);
    }
}
