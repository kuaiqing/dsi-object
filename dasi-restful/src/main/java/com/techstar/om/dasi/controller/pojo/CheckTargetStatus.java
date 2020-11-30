package com.techstar.om.dasi.controller.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.techstar.om.dasi.domain.ECheckTargetType;
import com.techstar.om.dasi.domain.EPriority;
import com.techstar.om.dasi.jpa.result.CheckResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckTargetStatus {
    private String id;
    private EPriority priority;
    private String hint;
    @JsonIgnore
    private String name;
    @JsonIgnore
    private String group;
    @JsonIgnore
    private ECheckTargetType targetType;

    public CheckTargetStatus(CheckResult result) {
        this.id = result.getTarget().getId().toString();
        this.priority = result.getPriority();
        this.hint = result.getHint();

        this.name = result.getTarget().getName();
        this.group = result.getTarget().getGroup().getName();
        this.targetType = result.getTarget().getTargetType();
    }
}
