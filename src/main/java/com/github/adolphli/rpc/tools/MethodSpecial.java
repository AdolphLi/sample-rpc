package com.github.adolphli.rpc.tools;

/**
 * 用于指定服务方法的超时时间
 */
public class MethodSpecial {

    // 方法名
    private String methodName;
    // 超时时间
    private int clientTimeout;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getClientTimeout() {
        return clientTimeout;
    }

    public void setClientTimeout(int clientTimeout) {
        this.clientTimeout = clientTimeout;
    }
}
