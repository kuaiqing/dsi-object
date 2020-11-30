package com.techstar.om.dasi.jpa.info;

import com.techstar.framework.jpa.common.JpaString;
import com.techstar.om.dasi.domain.ECheckTargetType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "om_check_handler_info")
public class CheckHandler extends JpaString {
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    private ECheckTargetType targetType;

    public CheckHandler() {
    }

    public CheckHandler(String id) {
        super(id);
    }
}
