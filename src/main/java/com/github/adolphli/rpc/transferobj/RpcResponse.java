package com.github.adolphli.rpc.transferobj;

import java.io.Serializable;

/**
 * rpc 调用的response封装类
 */
public class RpcResponse implements Serializable {

    private String errorMsg;
    private boolean isError;
    private Object response;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {

        if (errorMsg == null) {
            return;
        }

        this.errorMsg = errorMsg;
        this.isError = true;
    }

    public boolean isError() {
        return isError;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
