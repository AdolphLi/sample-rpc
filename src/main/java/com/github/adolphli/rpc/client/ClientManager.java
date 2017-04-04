package com.github.adolphli.rpc.client;

import com.github.adolphli.netty.wrapper.Client;
import com.github.adolphli.netty.wrapper.DefaultCliet;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * rpc调用客户端的管理类
 */
public class ClientManager {

    private static final ConcurrentMap<String, Client> clients = new ConcurrentHashMap<String, Client>();

    public static Client getClient(String targetUrl) throws Exception {

        Client client = clients.get(targetUrl);
        if (client == null) {

            // 这里不加锁，因为即使多创建了一个client也是可以接受的
            String[] args = targetUrl.split(":");
            clients.putIfAbsent(targetUrl, new DefaultCliet(args[0], Integer.parseInt(args[1])));
            client = clients.get(targetUrl);
        }

        return client;
    }
}
