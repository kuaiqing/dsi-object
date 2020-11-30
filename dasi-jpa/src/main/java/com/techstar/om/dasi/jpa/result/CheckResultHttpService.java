package com.techstar.om.dasi.jpa.result;

import com.techstar.om.dasi.domain.EContentType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "om_check_result_http_service")
public class CheckResultHttpService extends CheckResultBase<CheckResultHttpServiceId> {
    @EmbeddedId
    private CheckResultHttpServiceId id;

    @Column(name = "url", nullable = false)
    private String url;
    @Column(name = "method", nullable = false)
    private String method;
    @Column(name = "http_status")
    private Integer httpStatus;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "content_type")
    private EContentType contentType;
    @Column(name = "result_data")
    private String resultData;

    public CheckResultHttpService() {
    }

    public CheckResultHttpService(CheckResultHttpServiceId id) {
        this.id = id;
    }

    public CheckResultHttpService(CheckResultHttpServiceId id, CheckResult result) {
        super(result);
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public void setValue(Double value) {

    }
}
