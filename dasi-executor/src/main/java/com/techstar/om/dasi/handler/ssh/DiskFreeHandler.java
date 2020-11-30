package com.techstar.om.dasi.handler.ssh;

import com.techstar.om.dasi.domain.ECheckTargetType;
import com.techstar.om.dasi.domain.EPriority;
import com.techstar.om.dasi.handler.SshHandler;
import com.techstar.om.dasi.handler.annotation.DsiHandler;
import com.techstar.om.dasi.jpa.result.CheckResult;
import com.techstar.om.dasi.jpa.result.CheckResultDiskFree;
import com.techstar.om.dasi.jpa.result.CheckResultDiskFreeId;
import com.techstar.om.dasi.param.SshParam;
import com.techstar.om.dasi.repos.result.CheckResultDiskFreeRepos;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@DsiHandler(id = "server_disk_free", name = "磁盘使用检查", type = ECheckTargetType.Server)
public class DiskFreeHandler extends SshHandler<CheckResultDiskFree, SshParam> {
    @Autowired
    private CheckResultDiskFreeRepos diskFreeRepos;

    @Override
    protected List<CheckResultDiskFree> processData(String data, SshParam param, CheckResult result) {
        List<CheckResultDiskFree> diskFrees = new ArrayList<>();
        if (result.getReturnStatus() == 0) { // 成功执行
            DateTime appliedTime = appliedTimeOf(result.getCheckTime(), param);
            String[] rows = data.split("\n");
            for (int i = 1; i < rows.length; i++) {
                String[] cols = rows[i].split("\\s+");
                if (cols.length >= 6) {
                    String mount = cols[5];
                    if (matched(mount, param.getMatches())) {
                        CheckResultDiskFreeId id = new CheckResultDiskFreeId(appliedTime, result.getId(), mount);
                        CheckResultDiskFree diskFree = new CheckResultDiskFree(id, result);
                        diskFree.setFilesystem(cols[0]);
                        diskFree.setSize(Integer.valueOf(cols[1]));
                        diskFree.setUsed(Integer.valueOf(cols[2]));
                        diskFrees.add(diskFree);
                    }
                }
            }
        }
        return diskFrees;
    }

    @Override
    public String commandOf(SshParam param) {
        return "df";
    }

    @Override
    protected void saveCheckResultData(List<CheckResultDiskFree> data) {
        diskFreeRepos.saveAll(data);
    }

    @Override
    protected void processResultData(List<CheckResultDiskFree> values, SshParam param, CheckResult result) {
        Map<String, List<CheckResultDiskFree>> mountDiskFrees = param.getMatches().stream().collect(Collectors.toMap(
                k -> k,
                v -> values.stream().filter(df -> matched(df.getId().getMount(), v)).collect(Collectors.toList())
        ));
        mountDiskFrees.forEach((k, v) -> {
            if (v.isEmpty()) { // 没有匹配到数据
                result.setPriority(EPriority.Error);
                result.setHint(String.format("%s,目录不存在", k));
            } else {
                v.forEach(value -> {
                    param.get("use").check(k, value.getUse(), result);
                });
            }
        });
    }

}
