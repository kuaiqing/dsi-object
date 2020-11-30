package com.techstar.om.dasi.handler;

import com.techstar.om.dasi.jpa.info.CheckPoint;
import com.techstar.om.dasi.jpa.info.CheckTarget;
import com.techstar.om.dasi.jpa.result.CheckResult;
import com.techstar.om.dasi.jpa.result.CheckResultBase;
import com.techstar.om.dasi.param.HttpParam;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.net.Socket;
import java.util.List;

public abstract class HttpHandler<T extends CheckResultBase, P extends HttpParam> extends HandlerBase<T, P> {
    @Override
    public String commandOf(P param) {
        return param.getUrl();
    }

    @Override
    public boolean test(CheckTarget target) throws Exception {
        Socket socket = new Socket(target.getHost(), target.getPort());
        try {
            return socket.isConnected();
        } finally {
            socket.close();
        }
    }

    @Override
    protected void execute(CheckPoint point, CheckResult result) throws Exception {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            execute(client, point, result);
        }
    }

    private void execute(HttpClient client, CheckPoint point, CheckResult result) throws Exception {
        point.setExecutorParams(internalParam(point.getExecutorParams()));
        P param = paramOf(point); // 获取参数
        HttpRequestBase request = httpRequestOf(point.getTarget(), param);
        request.setConfig(RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                .setSocketTimeout(point.getExecutorTimeout() * 1000).build());
        param.getHeaders().forEach(nameValue -> { // 请求头
            request.addHeader(nameValue.getName(), nameValue.getValue());
        });
        HttpResponse response = client.execute(request);

        int status = response.getStatusLine().getStatusCode();
        result.setReturnStatus(status);
        HttpEntity entity = response.getEntity();
        String data = EntityUtils.toString(entity);
        result.setReturnData(data);

        List<T> resultData = processData(entity, param, result);
        saveCheckResultData(resultData);
        processResultData(resultData, param, result);
    }

    private HttpRequestBase httpRequestOf(CheckTarget target, P param) throws Exception {
        String url = commandOf(param);
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme(Boolean.TRUE.equals(target.getSsl()) ? "https" : "http")
                .setHost(target.getHost()).setPort(target.getPort()).setPath(url);
        switch (param.getMethod()) {
            case HttpGet.METHOD_NAME:
                uriBuilder.setParameters(param.namedParams());
                return new HttpGet(uriBuilder.build());
            case HttpPost.METHOD_NAME:
                HttpPost httpPost = new HttpPost(uriBuilder.build());
                UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(param.getNamedParams(), "UTF-8");
                httpPost.setEntity(entityParam);
                return httpPost;
            default:
                throw new RuntimeException("不支持的HTTP方法:" + param.getMethod());
        }

    }

    protected abstract List<T> processData(HttpEntity entity, P param, CheckResult result) throws Exception;
}
