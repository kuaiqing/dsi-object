package com.techstar.om.dasi.domain;

import lombok.Getter;

@Getter
public enum ECheckTargetType {
    Server("服务器"), //
    Http("HTTP服务"), //
    Db("数据库"), //
    ; //

    private String name;

    private ECheckTargetType(String name) {
        this.name = name;
    }
}
