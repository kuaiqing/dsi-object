package com.techstar.om.dasi.jpa.result;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "om_check_result_disk_free")
public class CheckResultDiskFree extends CheckResultBase<CheckResultDiskFreeId> {
    @EmbeddedId
    private CheckResultDiskFreeId id;

    @Column(name = "filesystem")
    private String filesystem;
    @Column(name = "size")
    private Integer size;
    @Column(name = "used")
    private Integer used;

    public CheckResultDiskFree() {
    }

    @Override
    public void setValue(Double value) {

    }

    public CheckResultDiskFree(CheckResultDiskFreeId id) {
        this.id = id;
    }

    public CheckResultDiskFree(CheckResultDiskFreeId id, CheckResult result) {
        super(result);
        this.id = id;
    }

    @Transient
    public Float getUse() {
        return (used * 1.0f) / size * 100.0f;
    }
}
