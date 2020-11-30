package com.techstar.om.dasi.handler.ssh;

import com.techstar.om.dasi.domain.ECheckTargetType;
import com.techstar.om.dasi.domain.EPriority;
import com.techstar.om.dasi.handler.SshHandler;
import com.techstar.om.dasi.handler.annotation.DsiHandler;
import com.techstar.om.dasi.jpa.result.CheckResult;
import com.techstar.om.dasi.jpa.result.CheckResultProcessStatus;
import com.techstar.om.dasi.jpa.result.CheckResultProcessStatusId;
import com.techstar.om.dasi.param.SshParam;
import com.techstar.om.dasi.repos.result.CheckResultProcessStatusRepos;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@DsiHandler(id = "server_process_status", name = "进程状态检查", type = ECheckTargetType.Server)
public class ProcessStatusHandler extends SshHandler<CheckResultProcessStatus, SshParam> {
    @Autowired
    private CheckResultProcessStatusRepos processStatusRepos;

    @Override
    protected List<CheckResultProcessStatus> processData(String data, SshParam param, CheckResult result) {
        List<CheckResultProcessStatus> processStatuses = new ArrayList<>();
        if (result.getReturnStatus() == 0) { // 成功执行
            DateTime appliedTime = appliedTimeOf(result.getCheckTime(), param);
            String[] rows = data.split("\n");
            for (int i = 1; i < rows.length; i++) {
                List<String> cols = Arrays.asList(rows[i].split("\\s+"));
                if (cols.size() >= 11) {
                    String cmd = StringUtils.join(cols.subList(10, cols.size()), " ");
                    if (cmd.length() > 200) {
                        cmd = cmd.substring(0, 200);
                    }
                    if (matched(cmd, param.getMatches())) {
                        CheckResultProcessStatusId id = new CheckResultProcessStatusId(appliedTime, result.getId(),
                                cmd, cols.get(0), Integer.valueOf(cols.get(1)));
                        CheckResultProcessStatus processStatus = new CheckResultProcessStatus(id, result);
                        processStatus.setCpu(Float.valueOf(cols.get(2)));
                        processStatus.setMem(Float.valueOf(cols.get(3)));
                        processStatus.setVsz(Integer.valueOf(cols.get(4)));
                        processStatus.setRss(Integer.valueOf(cols.get(5)));
                        processStatus.setStart(cols.get(8));
                        processStatus.setTime(cols.get(9));
                        processStatuses.add(processStatus);
                    }
                }
            }
        }
        return processStatuses;
    }

    @Override
    public String commandOf(SshParam param) {
        return "ps -aux";
    }

    @Override
    protected void saveCheckResultData(List<CheckResultProcessStatus> data) {
        processStatusRepos.saveAll(data);
    }

    @Override
    protected void processResultData(List<CheckResultProcessStatus> values, SshParam param, CheckResult result) {
        Map<String, List<CheckResultProcessStatus>> cmdStatuses = param.getMatches().stream().collect(Collectors.toMap(
                k -> k,
                v -> values.stream().filter(df -> matched(df.getId().getCommand(), v)).collect(Collectors.toList())
        ));
        cmdStatuses.forEach((k, v) -> {
            if (v.isEmpty()) { // 没有匹配到数据
                result.setPriority(EPriority.Error);
                result.setHint(String.format("%s,进程不存在", k));
            } else {
                v.forEach(value -> {
                    param.get("mem").check(k, value.getMem(), result);
                    param.get("cpu").check(k, value.getCpu(), result);
                });
            }
        });
    }
}
