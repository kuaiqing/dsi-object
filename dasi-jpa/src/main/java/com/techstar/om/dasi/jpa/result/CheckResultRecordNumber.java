package com.techstar.om.dasi.jpa.result;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "om_check_result_record_number")
public class CheckResultRecordNumber extends CheckResultBase<CheckResultRecordNumberId> {
    @EmbeddedId
    private CheckResultRecordNumberId id;

    @Column(name = "value")
    private Double value;
    @Column(name = "compare")
    private Double compare;

    public CheckResultRecordNumber() {
    }

    public CheckResultRecordNumber(CheckResultRecordNumberId id) {
        this.id = id;
    }

    public CheckResultRecordNumber(CheckResultRecordNumberId id, CheckResult result) {
        super(result);
        this.id = id;
    }
}
