package com.techstar.om.dasi.handler;

import com.techstar.om.dasi.domain.EPriority;
import com.techstar.om.dasi.domain.InternalParam;
import com.techstar.om.dasi.jackson.JacksonMapper;
import com.techstar.om.dasi.jpa.info.CheckPoint;
import com.techstar.om.dasi.jpa.info.CheckTarget;
import com.techstar.om.dasi.jpa.result.CheckResult;
import com.techstar.om.dasi.jpa.result.CheckResultBase;
import com.techstar.om.dasi.param.ParamBase;
import com.techstar.om.dasi.repos.result.CheckResultRepos;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Log4j2
public abstract class HandlerBase<T extends CheckResultBase, P extends ParamBase> {

    @Autowired
    private CheckResultRepos checkResultRepos;

    // 执行检查操作，保存检查结果
    public void execute(CheckPoint point) {
        CheckResult result = new CheckResult(point);
        result.setTarget(point.getTarget());
        result.setCheckTime(DateTime.now());
        result.setPriority(EPriority.Info);
        try {
            execute(point, result);
        } catch (Exception e) {
            log.error(e);
            result.setReturnData(e.getMessage());
            result.setReturnStatus(-1);
            result.setPriority(EPriority.Error);
            result.setHint("检查执行异常");
        } finally {
            checkResultRepos.save(result);
        }
    }

    // 处理内部参数
    protected String internalParam(String params) {
        for (InternalParam param : InternalParam.values()) {
            String p = "@{" + param.name() + "}";
            params = params.replace(p, param.val());
        }
        return params;
    }

    // 脚本检查
    protected Boolean scriptRun(String script, Object value) {
        if (StringUtils.isNotEmpty(script)) {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
            SimpleBindings bindings = new SimpleBindings();
            if (null != value) {
                bindings.put("value", value);
            }
            try {
                return (Boolean) engine.eval(script, bindings);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    // 对象名称匹配，支持后模糊
    protected boolean matched(String object, String match) {
        if (match.endsWith("*")) {
            return object.startsWith(match.substring(0, match.length() - 1));
        } else {
            return match.equals(object);
        }
    }

    protected boolean matched(String object, List<String> matches) {
        for (String match : matches) {
            if (matched(object, match)) {
                return true;
            }
        }
        return false;
    }

    // 返回参数对象
    protected P paramOf(CheckPoint point) throws IOException {
        Type t = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        P param = JacksonMapper.instance.objectMapper.readValue(point.getExecutorParams(), (Class<P>) t);
        param.setAppliedPeriod(point.getAppliedPeriod());
        return param;
    }

    // 生成appliedTime，最小1小时，最大1天
    protected DateTime appliedTimeOf(DateTime time, P param) {
        int period = param.getAppliedPeriod();
        time = time.withMillisOfSecond(0).withSecondOfMinute(0).withMinuteOfHour(0);
        return time.withHourOfDay((time.getHourOfDay() / period) * period);
    }

    // 返回执行命令
    public abstract String commandOf(P param);

    // 测试连接是否正常
    public abstract boolean test(CheckTarget target) throws Exception;

    // 根据检查点，执行检查操作，将结果存储到CheckResult
    protected abstract void execute(CheckPoint point, CheckResult result) throws Exception;

    // 保存检查结果
    protected abstract void saveCheckResultData(List<T> data);

    // 处理检查结果，并生成告警级别和提示
    protected abstract void processResultData(List<T> values, P param, CheckResult result);
}
