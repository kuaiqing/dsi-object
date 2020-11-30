package com.techstar.om.dasi.handler.sql;

import com.techstar.om.dasi.domain.ECheckTargetType;
import com.techstar.om.dasi.handler.SqlHandler;
import com.techstar.om.dasi.handler.annotation.DsiHandler;
import com.techstar.om.dasi.jackson.JacksonMapper;
import com.techstar.om.dasi.jpa.result.CheckResult;
import com.techstar.om.dasi.jpa.result.CheckResultRecordNumber;
import com.techstar.om.dasi.jpa.result.CheckResultRecordNumberId;
import com.techstar.om.dasi.param.SqlParam;
import com.techstar.om.dasi.repos.result.CheckResultRecordNumberRepos;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
@DsiHandler(id = "db_record_number", name = "记录数量检查", type = ECheckTargetType.Db)
public class RecordNumberHandler extends SqlHandler<CheckResultRecordNumber, SqlParam> {
    @Autowired
    private CheckResultRecordNumberRepos recordNumberRepos;

    @Override
    protected List<CheckResultRecordNumber> processData(List<Map<String, Object>> data, SqlParam param, CheckResult result) throws Exception {
        List<CheckResultRecordNumber> recordCounts = new ArrayList<>();
        DateTime appliedTime = appliedTimeOf(result.getCheckTime(), param), lastAppliedTime = null;
        Map<CheckResultRecordNumberId, CheckResultRecordNumber> lastAppliedData = null;
        if (param.isLastCompare()) { // 获取上一个周期的存储结果
            CheckResultRecordNumber lastRecord = recordNumberRepos.findFirstByIdPointAndIdAppliedTimeLessThan(result.getId(), appliedTime);
            if (null != lastRecord) {
                lastAppliedTime = lastRecord.getId().getAppliedTime();
                lastAppliedData = recordNumberRepos.findByIdPointAndIdAppliedTime(result.getId(), lastAppliedTime).stream()
                        .collect(toMap(k -> k.getId(), v -> v));
            }
        }
        for (Map<String, Object> row : data) {
            String object = row.get("object").toString(), metric = row.get("metric").toString();
            CheckResultRecordNumberId id = new CheckResultRecordNumberId(appliedTime, result.getId(), object, metric);
            CheckResultRecordNumber recordCount = new CheckResultRecordNumber(id, result);
            recordCount.setValue(Double.valueOf(row.get("value").toString()));
            Object compare = row.get("compare");
            if (compare != null) {
                recordCount.setCompare(Double.valueOf(compare.toString()));
            } else if (lastAppliedData != null) {
                CheckResultRecordNumber lastData = lastAppliedData.get(new CheckResultRecordNumberId(
                        lastAppliedTime, result.getId(), object, metric));
                if (lastData != null) {
                    recordCount.setCompare(lastData.getValue());
                }
            }
            recordCounts.add(recordCount);
        }
        result.setReturnStatus(recordCounts.size());
        result.setReturnData(JacksonMapper.instance.objectMapper.writeValueAsString(recordCounts));
        return recordCounts;
    }

    @Override
    public String commandOf(SqlParam param) {
        return param.getSql();
    }

    @Override
    protected void saveCheckResultData(List<CheckResultRecordNumber> data) {
        recordNumberRepos.saveAll(data);
    }

    @Override
    protected void processResultData(List<CheckResultRecordNumber> values, SqlParam param, CheckResult result) {
        values.forEach(x -> {
            String object = x.getId().getObject() + "." + x.getId().getMetric();
            param.get("number").check(object, x.getValue(), result);
            param.get("increase").check(object,
                    x.getCompare() == null ? 0d : ((x.getValue() - x.getCompare()) / x.getCompare()),
                    result);
        });
    }

}
