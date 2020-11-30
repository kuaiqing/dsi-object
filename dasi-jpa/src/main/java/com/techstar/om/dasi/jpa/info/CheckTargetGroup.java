package com.techstar.om.dasi.jpa.info;

import com.techstar.framework.jpa.common.JpaGeneratedObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "om_check_target_group_info")
public class CheckTargetGroup extends JpaGeneratedObject {

    @Column(name = "code")
    private String code;

    public CheckTargetGroup() {
    }

    public CheckTargetGroup(Long id) {
        super(id);
    }
}
