package com.github.adolphli.rpc;

import com.github.adolphli.rpc.client.RpcConsumerBean;
import com.github.adolphli.rpc.tools.MethodSpecial;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * rpc客户端api类
 */
public class ConsumerService {

    // rpcConsumerBean实例
    private RpcConsumerBean consumer;
    // 标识是否初始化
    private AtomicBoolean inited = new AtomicBoolean(false);
    // 标识是否订阅
    private AtomicBoolean consumed = new AtomicBoolean(false);
    // methodSpecial列表
    private List<MethodSpecial> methodSpecials = new LinkedList<MethodSpecial>();

    /**
     * 初始化ConsumerService
     *
     * @return
     */
    public ConsumerService newConsumer() {
        if (inited.compareAndSet(false, true)) {
            consumer = new RpcConsumerBean();
            return this;
        }

        throw new RuntimeException("do not init ConsumerService twice");
    }

    /**
     * 设置要提供的RPC的服务名
     *
     * @param service RPC服务名
     * @return
     */
    public ConsumerService service(String service) {
        checkInited();
        consumer.setServiceInterface(service);
        return this;
    }

    /**
     * 设置要提供的RPC的服务ID
     *
     * @param serviceId RPC服务名Id
     * @return
     */
    public ConsumerService serviceId(String serviceId) {
        checkInited();
        consumer.setServiceId(serviceId);
        return this;
    }

    /**
     * 统一设置所有服务的超时时间
     *
     * @param timeout 超时时间
     * @return
     */
    public ConsumerService timeout(int timeout) {
        checkInited();
        consumer.setClientTimeout(timeout);
        return this;
    }

    /**
     * 针对某个方法设置单独的超时时间
     *
     * @param methodName 方法名
     * @param timeout    对应方法的超时时间
     * @return
     */
    public ConsumerService methodTimeout(String methodName, int timeout) {
        checkInited();
        MethodSpecial ms = new MethodSpecial();
        ms.setMethodName(methodName);
        ms.setClientTimeout(timeout);
        synchronized (methodSpecials) {
            methodSpecials.add(ms);
        }
        return this;
    }

    /**
     * 指定IP调用
     *
     * @param url 指定的url
     * @return
     */
    public ConsumerService targetUrl(String url) {
        checkInited();
        consumer.setTargetUrl(url);
        return this;
    }

    /**
     * 获取消费服务的接口，可以进行强制转换。
     *
     * @return
     */
    public Object consume() {
        checkInited();
        try {
            if (consumed.compareAndSet(false, true)) {
                consumer.setMethodSpecials(methodSpecials);
                consumer.init();
            }
            return consumer.getTarget();
        } catch (Exception e) {
            throw new RuntimeException("consume error", e);
        }
    }

    private void checkInited() {
        if (inited.compareAndSet(true, true))
            return;

        throw new RuntimeException("ConsumerService has not been inited");
    }
}
