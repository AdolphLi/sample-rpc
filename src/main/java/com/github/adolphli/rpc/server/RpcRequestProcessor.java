package com.github.adolphli.rpc.server;

import com.github.adolphli.netty.wrapper.rpc.HandlerContext;
import com.github.adolphli.netty.wrapper.rpc.RequestProcessor;
import com.github.adolphli.rpc.transferobj.RpcRequest;
import com.github.adolphli.rpc.transferobj.RpcResponse;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * RequestProcessor实现类，用于处理RpcRequest请求
 */
public class RpcRequestProcessor implements RequestProcessor<RpcRequest> {

    private static final Executor executor = new ThreadPoolExecutor(100, 200, 100, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(1000), new ThreadFactoryBuilder().setNameFormat("rpc-runner-%d").build());

    @Override
    public void handleRequest(HandlerContext handlerContext, RpcRequest request) {

        RpcResponse response;
        try {
            String serviceName = request.getUniqueServiceName();
            Object implementationClass = RpcServer.getInstance().getImplementationClass(serviceName);
            Method method = RpcServer.getInstance().getMethod(serviceName, getMethodSig(request));
            Object result = method.invoke(implementationClass, request.getMethodArgs());
            response = new RpcResponse();
            response.setResponse(result);
        } catch (Throwable e) {
            response = createErrorRespnse(e.getMessage());
        }

        try {
            handlerContext.sendResponse(response);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private String getMethodSig(RpcRequest request) {

        StringBuilder sb = new StringBuilder(request.getMethodName());
        for (String argSig : request.getMethodArgSigs()) {
            sb.append(argSig);
        }
        return sb.toString();
    }

    private RpcResponse createErrorRespnse(String message) {
        RpcResponse response = new RpcResponse();
        response.setErrorMsg(message);
        return response;
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    @Override
    public String interest() {
        return RpcRequest.class.getName();
    }
}
