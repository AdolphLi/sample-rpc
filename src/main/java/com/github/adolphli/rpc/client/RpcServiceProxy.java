package com.github.adolphli.rpc.client;

import com.github.adolphli.netty.wrapper.Client;
import com.github.adolphli.rpc.transferobj.RpcRequest;
import com.github.adolphli.rpc.transferobj.RpcResponse;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * rpc调用代理对象， 所有rpc调用均会被此类代理
 */
public class RpcServiceProxy implements MethodInterceptor {

    private final RpcConsumerBean consumer;

    public RpcServiceProxy(RpcConsumerBean rpcConsumer) {
        this.consumer = rpcConsumer;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        RpcRequest request = createRequest(methodInvocation);
        final Client client = ClientManager.getClient(consumer.getTargetUrl());
        RpcResponse response = (RpcResponse) client.invokeSync(request, consumer.getTimeout(methodInvocation.getMethod().getName()));
        if (response.isError()) {
            throw new RuntimeException(response.getErrorMsg());
        }
        return response.getResponse();
    }

    /**
     * 根据methodInvocation信息创建RpcRequest对象
     *
     * @param methodInvocation
     * @return
     */
    private RpcRequest createRequest(MethodInvocation methodInvocation) {

        Method method = methodInvocation.getMethod();
        String methodName = method.getName();
        Object[] args = methodInvocation.getArguments();

        RpcRequest request = new RpcRequest();
        request.setMethodArgs(args);
        request.setMethodName(methodName);
        request.setMethodArgSigs(createArgSigs(method.getParameterTypes()));
        request.setUniqueServiceName(consumer.getUniqueServiceName());
        return request;
    }

    private String[] createArgSigs(Class<?>[] parameterTypes) {

        if (parameterTypes == null || parameterTypes.length == 0) {
            return new String[]{};
        }

        String[] params = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            params[i] = parameterTypes[i].getName();
        }
        return params;
    }
}
