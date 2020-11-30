package com.techstar.om.dasi.controller.pojo;

import com.techstar.om.dasi.domain.ECheckTargetType;
import com.techstar.om.dasi.domain.EPriority;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
public class CheckResultSummary {
    private Map<ECheckTargetType, Map<EPriority, Long>> alerts; // 对象类型 > 告警 > 数量
    private Map<ECheckTargetType, Map<String, Map<String, CheckTargetStatus>>> statuses; // 对象类型 > 分组 > 对象 > 对象状态
}
