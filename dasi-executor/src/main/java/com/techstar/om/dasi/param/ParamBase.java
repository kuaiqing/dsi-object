package com.techstar.om.dasi.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class ParamBase {
    @Getter
    @Setter
    private int appliedPeriod = 1; // 从point的appliedPeriod获取，为了参数传递方便
    // 检查参数
    private final Map<String, MetricLimit<String>> checks = new HashMap<>();

    public Collection<MetricLimit<String>> getChecks() {
        return checks.values();
    }

    public void setChecks(Collection<MetricLimit<String>> checks) {
        this.checks.putAll(checks.stream().collect(Collectors.toMap(k -> k.getCode(), v -> v)));
    }

    @JsonIgnore
    public MetricLimit<String> get(String code) {
        return Optional.ofNullable(checks.get(code)).orElseGet(() -> new MetricLimit<>());
    }
}
