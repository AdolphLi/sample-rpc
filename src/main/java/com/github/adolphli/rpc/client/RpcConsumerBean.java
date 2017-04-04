package com.github.adolphli.rpc.client;

import com.github.adolphli.rpc.tools.MethodSpecial;
import org.springframework.aop.framework.ProxyFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * rpc 客户端属性封装类
 */
public class RpcConsumerBean {

    // 订阅接口名
    private String serviceInterface;
    // 订阅接口ID
    private String serviceId;
    // 服务超时时间
    private int clientTimeout;
    // 方法超时时间
    private final Map<String, Integer> methodSpecials = Collections.synchronizedMap(new HashMap<String, Integer>());
    // 目标地址
    private String targetUrl;

    private static final int DEFAULT_TIMEOUT = 3000;

    /**
     * 得到服务的唯一标识
     * 如果设置了serviceId属性， 服务唯一标识为：serviceInterface + ":" + serviceId
     * 否则为serviceInterface
     *
     * @return
     */
    public String getUniqueServiceName() {
        if (serviceId != null && serviceId.length() > 0) {
            return serviceInterface + ":" + serviceId;
        }

        return serviceInterface;
    }

    /**
     * 得到方法超时时间
     *
     * @param methodName 方法名
     * @return
     */
    public long getTimeout(String methodName) {
        Integer timeout = methodSpecials.get(methodName);
        if (timeout != null) {
            return timeout;
        }

        if (clientTimeout != 0) {
            return clientTimeout;
        }

        return DEFAULT_TIMEOUT;
    }

    /**
     * 得到代理对象， 此代理对象已经实现serviceInterface接口
     *
     * @return
     * @throws Exception
     */
    public Object getTarget() throws Exception {

        ProxyFactory factory = new ProxyFactory();
        Class javaClass = Class.forName(serviceInterface);
        if (javaClass.isInterface()) {
            factory.addInterface(javaClass);
        } else {
            factory.setTargetClass(javaClass);
            factory.setProxyTargetClass(true);
        }
        factory.addAdvice(new RpcServiceProxy(this));

        return factory.getProxy();
    }

    /**
     * 初始化RpcConsumerBean
     */
    public void init() {
        // Do nothing
    }

    public void setServiceInterface(String interfaceName) {
        this.serviceInterface = interfaceName;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setClientTimeout(int clientTimeout) {
        this.clientTimeout = clientTimeout;
    }

    public void setMethodSpecials(List<MethodSpecial> methodSpecials) {

        for (MethodSpecial methodSpecial : methodSpecials) {
            this.methodSpecials.put(methodSpecial.getMethodName(), methodSpecial.getClientTimeout());
        }
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }
}
