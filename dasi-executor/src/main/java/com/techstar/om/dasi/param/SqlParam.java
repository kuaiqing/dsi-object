package com.techstar.om.dasi.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlParam extends ParamBase {
    private String sql; // 查询语句
    private boolean lastCompare; // 与最近一次存储周期的查询结果进行对比
}
