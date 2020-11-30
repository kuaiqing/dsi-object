package com.techstar.om.dasi.handler;

import com.techstar.om.dasi.jackson.JacksonMapper;
import com.techstar.om.dasi.jpa.info.CheckPoint;
import com.techstar.om.dasi.jpa.info.CheckTarget;
import com.techstar.om.dasi.jpa.result.CheckResult;
import com.techstar.om.dasi.jpa.result.CheckResultBase;
import com.techstar.om.dasi.param.SqlParam;

import java.sql.*;
import java.util.*;

public abstract class SqlHandler<T extends CheckResultBase, P extends SqlParam> extends HandlerBase<T, P> {

    private Connection getConnection(CheckTarget target) throws Exception {
        return DriverManager.getConnection(target.getUrl(), target.getUsername(), target.password());
    }

    @Override
    public boolean test(CheckTarget target) throws Exception {
        try (Connection connection = getConnection(target)) {
            return true;
        }
    }

    @Override
    public void execute(CheckPoint point, CheckResult result) throws Exception {
        try (Connection connection = getConnection(point.getTarget())) {
            try (Statement st = connection.createStatement()) {
                st.setQueryTimeout(point.getExecutorTimeout());
                execute(st, point, result);
            }
        }
    }

    private void execute(Statement st, CheckPoint point, CheckResult result) throws Exception {
        point.setExecutorParams(internalParam(point.getExecutorParams()));
        P param = paramOf(point); // 获取参数
        String cmd = commandOf(param);

        ResultSet rs = st.executeQuery(cmd);
        List<Map<String, Object>> data = rs2map(rs);
        result.setReturnStatus(data.size());
        result.setReturnData(JacksonMapper.instance.objectMapper.writeValueAsString(data));

        List<T> resultData = processData(data, param, result);
        saveCheckResultData(resultData);
        processResultData(resultData, param, result);
    }

    protected List<Map<String, Object>> rs2map(ResultSet rs) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        while (rs.next()) {
            Map<String, Object> rowData = new HashMap<>();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                rowData.put(meta.getColumnName(i), rs.getObject(i));
            }
            result.add(rowData);
        }
        return result;
    }

    protected abstract List<T> processData(List<Map<String, Object>> data, P param, CheckResult result) throws Exception;
}
