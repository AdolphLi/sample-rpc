package com.github.adolphli.rpc;

import com.github.adolphli.rpc.server.RpcProviderBean;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * rpc服务端api类
 */
public class ProviderService {

    // rpcProviderBean 实例
    private RpcProviderBean provider;
    // 标识是否初始化
    private AtomicBoolean inited = new AtomicBoolean(false);
    // 标识是否发布
    private AtomicBoolean published = new AtomicBoolean(false);

    /**
     * 初始化 ProviderService
     *
     * @return
     */
    public ProviderService newProvider() {
        if (inited.compareAndSet(false, true)) {
            provider = new RpcProviderBean();
            return this;
        }
        throw new RuntimeException("do not init ProviderService twice");
    }

    /**
     * 设置要提供的RPC服务名
     *
     * @param service RPC服务名
     * @return
     */
    public ProviderService service(String service) {
        checkInited();
        provider.setServiceInterface(service);
        return this;
    }

    /**
     * 设置要提供的RPC服务ID
     *
     * @param serviceId RPC服务名ID
     * @return
     */
    public ProviderService serviceId(String serviceId) {
        checkInited();
        provider.setServiceId(serviceId);
        return this;
    }

    /**
     * 设置要提供的RPC服务的接口实现类，这里要传入一个对象
     *
     * @param impl RPC服务接口对应的实现类
     * @return
     */
    public ProviderService impl(Object impl) {
        checkInited();
        provider.setTarget(impl);
        return this;
    }

    /**
     * 发布服务。
     */
    public void publish() {
        try {
            if (published.compareAndSet(false, true)) {
                checkInited();
                provider.init();
            }
        } catch (Exception e) {
            throw new RuntimeException("publish error");
        }
    }

    private void checkInited() {
        if (inited.compareAndSet(true, true))
            return;
        throw new RuntimeException("ProviderService has not been init");
    }
}
