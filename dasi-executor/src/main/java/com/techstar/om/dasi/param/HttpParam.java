package com.techstar.om.dasi.param;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
public class HttpParam extends ParamBase {
    private String url; // 请求地址
    private String method = HttpGet.METHOD_NAME; // 请求方法，只支持GET和POST
    private Integer httpStatus = HttpStatus.SC_OK; // http正常返回值

    private List<BasicNameValuePair> namedParams = new ArrayList<>(); // 命名参数
    private List<BasicNameValuePair> headers = new ArrayList<>(); // 请求头

    public List<NameValuePair> namedParams() {
        return namedParams.stream().map(x -> (NameValuePair) x).collect(toList());
    }

    @Getter
    @Setter
    public static class BasicNameValuePair implements NameValuePair, Cloneable, Serializable {
        private String name;
        private String value;

        public BasicNameValuePair() {
        }

        public BasicNameValuePair(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

}
