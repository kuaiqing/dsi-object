package com.techstar.om.dasi.handler.http;

import cn.hutool.core.util.XmlUtil;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.techstar.om.dasi.domain.ECheckTargetType;
import com.techstar.om.dasi.domain.EContentType;
import com.techstar.om.dasi.domain.EPriority;
import com.techstar.om.dasi.handler.HttpHandler;
import com.techstar.om.dasi.handler.annotation.DsiHandler;
import com.techstar.om.dasi.jpa.result.CheckResult;
import com.techstar.om.dasi.jpa.result.CheckResultHttpService;
import com.techstar.om.dasi.jpa.result.CheckResultHttpServiceId;
import com.techstar.om.dasi.param.HttpParam;
import com.techstar.om.dasi.repos.result.CheckResultHttpServiceRepos;
import org.apache.http.HttpEntity;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.xpath.XPathConstants;
import java.util.Collections;
import java.util.List;

@Service
@DsiHandler(id = "http_service", name = "HTTP服务检查", type = ECheckTargetType.Http)
public class HttpServiceHandler extends HttpHandler<CheckResultHttpService, HttpParam> {
    @Autowired
    private CheckResultHttpServiceRepos httpServiceRepos;

    @Override
    protected List<CheckResultHttpService> processData(HttpEntity entity, HttpParam param, CheckResult result) throws Exception {
        DateTime appliedTime = appliedTimeOf(result.getCheckTime(), param);
        CheckResultHttpServiceId id = new CheckResultHttpServiceId(appliedTime, result.getId());

        CheckResultHttpService httpService = new CheckResultHttpService(id, result);
        httpService.setUrl(param.getUrl());
        httpService.setMethod(param.getMethod());
        httpService.setHttpStatus(result.getReturnStatus());

        EContentType contentType = EContentType.typeOf(entity.getContentType().getValue());
        httpService.setContentType(contentType);
        httpService.setResultData(result.getReturnData());
        return Collections.singletonList(httpService);
    }

    @Override
    protected void saveCheckResultData(List<CheckResultHttpService> data) {
        httpServiceRepos.saveAll(data);
    }

    @Override
    protected void processResultData(List<CheckResultHttpService> values, HttpParam param, CheckResult result) {
        values.forEach(x -> {
            if (!x.getHttpStatus().equals(param.getHttpStatus())) {
                result.setPriority(EPriority.Error);
                result.setHint(String.format("HTTP返回值：%d，期望值：%d", x.getHttpStatus(), param.getHttpStatus()));
                return;
            }
            switch (x.getContentType()) {
                case JSON:
                    jsonCheck(x.getResultData(), param, result);
                    break;
                case XML:
                    xmlCheck(x.getResultData(), param, result);
                    break;
                default:
                    textCheck(x.getResultData(), param, result);
            }
        });
    }

    private void jsonCheck(String data, HttpParam param, CheckResult result) {
        DocumentContext doc = JsonPath.parse(data);
        param.getChecks().forEach((limit) -> {
            Object value = doc.read(limit.getCode());
            limit.check(limit.getCode(), value, result);
        });
    }

    private void xmlCheck(String data, HttpParam param, CheckResult result) {
        Document doc = XmlUtil.readXML(data);
        param.getChecks().forEach((limit) -> {
            Object value = XmlUtil.getByXPath(limit.getCode(), doc, XPathConstants.STRING);
            limit.check(limit.getCode(), value, result);
        });
    }

    private void textCheck(String data, HttpParam param, CheckResult result) {
        param.getChecks().forEach((limit) -> {
            limit.check(limit.getCode(), data, result);
        });
    }

}
