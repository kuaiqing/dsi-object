package com.techstar.om.dasi.wrapper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
public class WrapperData extends WrapperResponse<WrapperData> {
    private Object data;
    private String message;
    private Date date;

    public static WrapperData wrap() {
        return wrap(null);
    }

    public static WrapperData wrap(Object data) {
        WrapperData wrapper = new WrapperData();
        wrapper.setData(data);
        wrapper.setDate(new Date());
        return wrapper;
    }

    public WrapperData message(String message) {
        this.message = message;
        return this;
    }

    public WrapperData code(int code) {
        this.code = code;
        return this;
    }

    @Override
    public boolean isSuccess() {
        return status <= HttpStatus.BAD_REQUEST.value();
    }
}