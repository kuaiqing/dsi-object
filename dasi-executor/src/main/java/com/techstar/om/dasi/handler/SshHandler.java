package com.techstar.om.dasi.handler;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.techstar.om.dasi.jpa.info.CheckPoint;
import com.techstar.om.dasi.jpa.info.CheckTarget;
import com.techstar.om.dasi.jpa.result.CheckResult;
import com.techstar.om.dasi.jpa.result.CheckResultBase;
import com.techstar.om.dasi.param.ParamBase;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public abstract class SshHandler<T extends CheckResultBase, P extends ParamBase> extends HandlerBase<T, P> {
    private final static String StreamCharset = Charset.defaultCharset().toString();


    @Override
    public boolean test(CheckTarget target) throws Exception {
        Connection connection = new Connection(target.getHost(), target.getPort());
        connection.connect();
        try {
            return connection.authenticateWithPassword(target.getUsername(), target.password());
        } finally {
            connection.close();
        }
    }

    @Override
    public void execute(CheckPoint point, CheckResult result) throws Exception {
        CheckTarget target = point.getTarget();
        Connection connection = new Connection(target.getHost(), target.getPort());
        connection.connect();
        try {
            if (!connection.authenticateWithPassword(target.getUsername(), target.password())) { // 连接到目标服务器
                throw new RuntimeException("SSH认证失败");
            }
            Session session = connection.openSession();
            try {
                execute(session, point, result);
            } finally {
                session.close();
            }
        } finally {
            connection.close();
        }
    }

    private void execute(Session session, CheckPoint point, CheckResult result) throws Exception {
        P param = paramOf(point); // 获取参数
        String cmd = commandOf(param);
        session.execCommand(cmd); // 执行命令
        String returnData = processSession(session, point.getExecutorTimeout() * 1000); // 获取返回数据
        int returnStatus = session.getExitStatus(); // 获取返回结果
        // 检查结果
        result.setReturnData(returnData);
        result.setReturnStatus(returnStatus);
        // 检查结果明细数据
        List<T> resultData = processData(returnData, param, result);
        saveCheckResultData(resultData);
        processResultData(resultData, param, result);
    }

    private String processSession(Session session, int timeout) throws Exception {
        InputStream stdErr = new StreamGobbler(session.getStderr());
        InputStream stdOut = new StreamGobbler(session.getStdout());
        session.waitForCondition(ChannelCondition.EXIT_STATUS, timeout * 1000);
        String val = processStream(stdErr, StreamCharset) + processStream(stdOut, StreamCharset);
        val = val.replaceAll("[\u0000]", "");
        return val;
    }

    private String processStream(InputStream in, String charset) throws Exception {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
    }

    protected abstract List<T> processData(String data, P param, CheckResult result);
}
