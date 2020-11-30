package com.techstar.om.dasi.domain;

import lombok.Getter;

@Getter
public enum EPriority {
    Info("正常"), //
    Warn("警告"), //
    Alarm("报警"), //
    Error("错误"), //

    Unknown("未知"); //

    private String name;

    private EPriority(String name) {
        this.name = name;
    }
}
