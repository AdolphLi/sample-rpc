package com.github.adolphli.rpc.server;

import com.github.adolphli.netty.wrapper.DefaultServer;
import com.github.adolphli.netty.wrapper.Server;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 服务端封装类
 */
public class RpcServer {

    // 服务端端口
    private static final int RPC_SERVER_PORT = 6666;

    // rpcServer
    private static RpcServer server;
    // ConcurrentHashMap， 用于存储实现类
    public static final ConcurrentMap<String, Object> implementationClasses = new ConcurrentHashMap<String, Object>();
    // ConcurrentHashMap， 用于存储方法
    public static final ConcurrentMap<String, Map<String, Method>> methods = new ConcurrentHashMap<String, Map<String, Method>>();

    /**
     * 得到 RpcServer 实例
     *
     * @return
     * @throws Exception
     */
    public static synchronized RpcServer getInstance() throws Exception {

        if (server == null) {
            server = new RpcServer();
        }
        return server;
    }

    /**
     * 将 RpcProviderBean 注册至服务端
     *
     * @param provider
     * @throws Exception
     */
    public void provide(RpcProviderBean provider) throws Exception {

        implementationClasses.put(provider.getUniqueServiceName(), provider.getTarget());

        Map<String, Method> targetMethods = new HashMap<String, Method>();
        for (Method m : provider.getServiceClass().getMethods()) {
            StringBuilder methodParams = new StringBuilder();
            methodParams.append(m.getName());
            for (Class param : m.getParameterTypes()) {
                methodParams.append(param.getName());
            }
            targetMethods.put(methodParams.toString(), m);
        }
        methods.put(provider.getUniqueServiceName(), targetMethods);
    }

    /**
     * 得到接口的实现类
     *
     * @return
     */
    public Object getImplementationClass(String serviceUniqueName) {

        return implementationClasses.get(serviceUniqueName);
    }

    /**
     * 根据methodSig及methodName 得到Method对象
     *
     * @return
     */
    public Method getMethod(String serviceUniqueName, String methodSig) throws Exception {

        return methods.get(serviceUniqueName).get(methodSig);
    }

    /**
     * 私有构造函数
     *
     * @throws Exception
     */
    private RpcServer() throws Exception {
        Server server = new DefaultServer(RPC_SERVER_PORT);
        server.registerRequestProcessor(new RpcRequestProcessor());
        server.start();
    }
}
