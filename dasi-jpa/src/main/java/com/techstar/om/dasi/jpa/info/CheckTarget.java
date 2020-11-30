package com.techstar.om.dasi.jpa.info;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.techstar.framework.jpa.common.JpaGeneratedObject;
import com.techstar.framework.jpa.jackson.ObjectSerializer;
import com.techstar.om.dasi.domain.ECheckTargetType;
import com.techstar.om.dasi.jpa.convert.DateTimeConverter;
import com.techstar.om.dasi.utils.AESUtils;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "om_check_target_info")
public class CheckTarget extends JpaGeneratedObject {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_group", nullable = false)
    @JsonSerialize(using = ObjectSerializer.IdName.class)
    private CheckTargetGroup group;
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    private ECheckTargetType targetType;
    @Column(name = "version")
    private String version;
    @Column(name = "host")
    private String host;
    @Column(name = "port")
    private Integer port;
    @Column(name = "url")
    private String url;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "ssl")
    private Boolean ssl;
    @Column(name = "disable")
    private Boolean disable;
    @Column(name = "update_time")
    @Convert(converter = DateTimeConverter.class)
    private DateTime updateTime;

    public CheckTarget() {
        updateTime = DateTime.now();
    }

    public CheckTarget(Long id) {
        super(id);
        updateTime = DateTime.now();
    }


    public String password() throws Exception {
        return AESUtils.decrypt(this.password);
    }
}
