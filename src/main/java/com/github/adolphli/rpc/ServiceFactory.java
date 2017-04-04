package com.github.adolphli.rpc;

import java.util.concurrent.ConcurrentHashMap;

/**
 * RPC服务工程类
 */
public class ServiceFactory {

    private static ServiceFactory factory;
    private static ConcurrentHashMap<String, ProviderService> providers = new ConcurrentHashMap<String, ProviderService>();
    private static ConcurrentHashMap<String, ConsumerService> consumers = new ConcurrentHashMap<String, ConsumerService>();

    private ServiceFactory() {
    }

    public static synchronized ServiceFactory getInstance() {
        if (factory == null)
            factory = new ServiceFactory();
        return factory;
    }

    /**
     * 获取一个RPC服务提供者实例，此方法会缓存提供过的实例
     *
     * @return
     */
    public ProviderService provider(String uniqueName) {
        if (providers.get(uniqueName) == null) {
            providers.put(uniqueName, new ProviderService().newProvider());
            return providers.get(uniqueName);
        } else {
            return providers.get(uniqueName);
        }
    }

    /**
     * 获取一个RPC服务消费者实例，此方法会缓存消费过的实例
     *
     * @return
     */
    public ConsumerService consumer(String uniqueName) {
        if (consumers.get(uniqueName) == null) {
            consumers.put(uniqueName, new ConsumerService().newConsumer());
            return consumers.get(uniqueName);
        } else {
            return consumers.get(uniqueName);
        }
    }
}
