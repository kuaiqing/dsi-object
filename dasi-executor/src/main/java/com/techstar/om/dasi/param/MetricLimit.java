package com.techstar.om.dasi.param;

import cn.hutool.core.util.NumberUtil;
import com.techstar.om.dasi.domain.EPriority;
import com.techstar.om.dasi.jpa.result.CheckResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Getter
@Setter
public class MetricLimit<T extends Comparable<T>> {
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.###");
    private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");

    private String code; // 测点编码
    private String name; // 测点名称

    private T highI; // 高限阈值I
    private T highII; // 高限阈值II

    private T lowI; // 低限阈值I
    private T lowII; // 低限阈值II

    public MetricLimit() {
    }

    public MetricLimit(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public MetricLimit(String code, String name, T highI, T highII) {
        this(code, name);
        this.highI = highI;
        this.highII = highII;
    }

    public MetricLimit(String code, String name, T highI, T highII, T lowI, T lowII) {
        this(code, name, highI, highII);
        this.lowI = lowI;
        this.lowII = lowII;
    }

    public MetricLimit<Float> floatValue() {
        return new MetricLimit<Float>(code, name,
                highI == null ? null : Float.valueOf(highI.toString()),
                highII == null ? null : Float.valueOf(highII.toString()),
                lowI == null ? null : Float.valueOf(lowI.toString()),
                lowII == null ? null : Float.valueOf(lowII.toString()));
    }

    /**
     * @param object   名称
     * @param value    // 值
     * @param limit    // 阈值
     * @param isHigh   // 是否上限
     * @param priority // 告警级别
     * @param result   // 检查结果
     * @return 越限或者错误，返回false
     */
    private boolean check(String object, Object value, T limit, boolean isHigh, EPriority priority, CheckResult result) {
        if (limit == null || StringUtils.isBlank(limit.toString())) { // limit为空，表示不判断
            return true;
        }
        String limitStr = limit.toString(), valueStr = value.toString();
        if (limit instanceof Number || NumberUtil.isNumber(limitStr)) { // 指定类型的数字
            valueStr = decimalFormat.format(value);
            BigDecimal lim = NumberUtil.toBigDecimal(limitStr), val = NumberUtil.toBigDecimal(valueStr);
            int compare = val.compareTo(lim);
            if (isHigh) {
                if (compare >= 0) { // 越上限
                    result.setPriority(priority);
                    result.setHint(String.format("%s[%s],%s≥%s", object, name, valueStr, limitStr));
                    return false;
                }
            } else {
                if (compare <= 0) { // 越下限
                    result.setPriority(priority);
                    result.setHint(String.format("%s[%s],%s≤%s", object, name, valueStr, limitStr));
                    return false;
                }
            }
        } else { // JS脚本
            SimpleBindings bindings = new SimpleBindings();
            bindings.put("val", value);
            try {
                Boolean res = (Boolean) engine.eval(limitStr, bindings);
                if (Boolean.TRUE.equals(res)) {
                    result.setPriority(priority);
                    result.setHint(String.format("%s[%s],%s", object, name, limitStr.replace("val", valueStr)));
                    return false;
                }
            } catch (Exception e) {
                result.setPriority(EPriority.Error);
                result.setHint(String.format("%s[%s],%s", object, name, e.getMessage()));
                return false;
            }
        }
//        result.setHint(String.format("%s[%s],%s[%s]", object, name, valueStr, limitStr));
        return true;
    }

    public void check(String object, Object value, CheckResult result) {
        if (null == value || StringUtils.isBlank(value.toString())) { // value不能为空
            result.setPriority(EPriority.Error);
            result.setHint(String.format("%s[%s],空值", object, name));
            return;
        }
        if (check(object, value, highII, true, EPriority.Alarm, result)) {
            if (check(object, value, highI, true, EPriority.Warn, result)) {
                if (check(object, value, lowII, false, EPriority.Alarm, result)) {
                    check(object, value, lowI, false, EPriority.Warn, result);
                }
            }
        }
    }

}
