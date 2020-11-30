package com.techstar.om.dasi.param;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SshParam extends ParamBase {
    // 检查对象匹配，可以使用*做后模糊匹配
    private List<String> matches = new ArrayList<>();

}
