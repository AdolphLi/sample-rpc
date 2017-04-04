package com.github.adolphli.rpc.transferobj;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * rpc 调用的request封装类
 */
public class RpcRequest implements Serializable {

    // rpc调用的方法名
    private String methodName;
    // rpc调用的服务名
    private String uniqueServiceName;
    // rpc调用的方法参数
    private Object[] methodArgs;
    // rpc调用的方法参数签名
    private String[] methodArgSigs;
    // 存放扩展属性
    private final Map<String, Object> requestProps = new HashMap<String, Object>();

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getMethodArgSigs() {
        return methodArgSigs;
    }

    public void setMethodArgSigs(String[] methodArgSigs) {
        this.methodArgSigs = methodArgSigs;
    }

    public Object[] getMethodArgs() {
        return methodArgs;
    }

    public void setMethodArgs(Object[] methodArgs) {
        this.methodArgs = methodArgs;
    }

    public String getUniqueServiceName() {
        return uniqueServiceName;
    }

    public void setUniqueServiceName(String serviceName) {
        this.uniqueServiceName = serviceName;
    }

    public Object getRequestProp(String key) {
        return requestProps.get(key);
    }

    public void addRequestProps(String key, Object value) {
        requestProps.put(key, value);
    }
}
