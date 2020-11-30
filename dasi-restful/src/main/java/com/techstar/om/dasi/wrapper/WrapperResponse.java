package com.techstar.om.dasi.wrapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class WrapperResponse<T extends WrapperResponse> {
    protected int code;
    protected int status = 200;

    public T status(int status) {
        this.status = status;
        return (T) this;
    }

    public abstract boolean isSuccess();
}