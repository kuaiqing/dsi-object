//package com.techstar.om.dasi;
//
//
//import com.techstar.om.dasi.handler.http.HttpServiceHandler;
//import com.techstar.om.dasi.handler.sql.RecordNumberHandler;
//import com.techstar.om.dasi.handler.ssh.DiskFreeHandler;
//import com.techstar.om.dasi.handler.ssh.ProcessStatusHandler;
//import com.techstar.om.dasi.jackson.JacksonMapper;
//import com.techstar.om.dasi.jpa.info.CheckPoint;
//import com.techstar.om.dasi.jpa.info.CheckTarget;
//import com.techstar.om.dasi.param.HttpParam;
//import com.techstar.om.dasi.param.HttpParam.BasicNameValuePair;
//import com.techstar.om.dasi.param.MetricLimit;
//import com.techstar.om.dasi.param.SqlParam;
//import com.techstar.om.dasi.param.SshParam;
//import com.techstar.om.dasi.utils.AESUtils;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.ArrayList;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ExecutorApplication.class)
//public class HandlerTest {
//    @Autowired
//    private DiskFreeHandler diskFreeHandler;
//    @Autowired
//    private ProcessStatusHandler processStatusHandler;
//    @Autowired
//    private RecordNumberHandler recordNumberHandler;
//    @Autowired
//    private HttpServiceHandler httpServiceHandler;
//
//    @Test
//    public void testDiskFree() throws Exception {
//        CheckPoint point = new CheckPoint(1L);
//        point.setTarget(linuxTarget());
//
//        SshParam diskFree = new SshParam();
//        diskFree.getMatches().add("/*");
//        point.setExecutorParams(JacksonMapper.instance.objectMapper.writeValueAsString(diskFree));
//
//        diskFreeHandler.execute(point);
//    }
//
//    @Test
//    public void testProcessStatus() throws Exception {
//        CheckPoint point = new CheckPoint(2L);
//        point.setTarget(linuxTarget());
//
//        SshParam processStatus = new SshParam();
//        processStatus.getMatches().add("/*");
//        point.setExecutorParams(JacksonMapper.instance.objectMapper.writeValueAsString(processStatus));
//
//        processStatusHandler.execute(point);
//    }
//
//    @Test
//    public void testRecordNumber() throws Exception {
//        CheckPoint point = new CheckPoint(3L);
//        point.setTarget(sqlTarget());
//
//        SqlParam recordCount = new SqlParam();
//        recordCount.setSql("select a.mount as object, 'disk.use' as metric, a.size as value, b.size as compare\n" +
//                "from public.om_check_result_disk_free a\n" +
//                "join public.om_check_result_disk_free b on a.applied_time=b.applied_time and a.check_target=b.check_target and a.mount=b.mount");
//        point.setExecutorParams(JacksonMapper.instance.objectMapper.writeValueAsString(recordCount));
//
//        recordNumberHandler.execute(point);
//    }
//
//    @Test
//    public void testHttpService() throws Exception {
//        CheckPoint point = new CheckPoint(4L);
//        point.setTarget(httpTarget());
//
//        HttpParam httpService = new HttpParam();
//        httpService.setUrl("/api/workItems");
//        httpService.getNamedParams().addAll(new ArrayList<BasicNameValuePair>() {{
//            new BasicNameValuePair("field", "id,author(login,fullName),creator(login,fullName),text,created,updated,duration(minutes),date,issue(id),usesMarkdown");
//            new BasicNameValuePair("top", "10");
//            new BasicNameValuePair("startDate", "2020-07-01");
//            new BasicNameValuePair("endDate", "2020-08-01");
//        }});
//        httpService.getHeaders().addAll(new ArrayList<BasicNameValuePair>() {{
//            new BasicNameValuePair("authorization", "Bearer perm:ZGV2MQ==.6YeH6ZuGeW91dHJhY2vkv6Hmga8=.b0x04UpA9hUAKBb0Mf9jTT6EoIKJN0");
//        }});
//        httpService.getChecks().addAll(new ArrayList<MetricLimit<String>>() {{
//            new MetricLimit<>("$.length()", "数量", "val < 3", "val < 5");
//        }});
//
//        point.setExecutorParams(JacksonMapper.instance.objectMapper.writeValueAsString(httpService));
//
//        httpServiceHandler.execute(point);
//    }
//
//
//    @Test
//    public void testPassword() throws Exception {
//        System.out.println(AESUtils.encrypt("techstar@12345"));
//        System.out.println(AESUtils.decrypt("+eMLufeLL8MbHMlPm6Xjww=="));
//    }
//
//
//    private static CheckTarget linuxTarget() throws Exception {
//        CheckTarget target = new CheckTarget(1L);
//        target.setHost("m0");
//        target.setPort(22);
//        target.setUsername("root");
//        target.setPassword(AESUtils.encrypt("tech@123"));
//        return target;
//    }
//
//    private static CheckTarget sqlTarget() throws Exception {
//        CheckTarget target = new CheckTarget(2L);
//        target.setUrl("jdbc:postgresql://172.16.206.36:5432/om");
//        target.setUsername("postgres");
//        target.setPassword(AESUtils.encrypt("123456"));
//        return target;
//    }
//
//    private static CheckTarget httpTarget() throws Exception {
//        CheckTarget target = new CheckTarget(3L);
//        target.setHost("space.techstar.com.cn");
//        target.setPort(8081);
//        return target;
//    }
//
//
//}
