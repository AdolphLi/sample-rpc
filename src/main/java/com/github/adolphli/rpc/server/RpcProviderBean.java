package com.github.adolphli.rpc.server;

/**
 * rpc 服务端属性封装类
 */
public class RpcProviderBean {
    // 发布接口名
    private String serviceInterface;
    // 发布接口ID
    private String serviceId;
    // 接口实现类
    private Object target;

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
     * 初始化RpcProviderBean，将RpcProviderBean注册至RpcServer
     */
    public void init() throws Exception {
        RpcServer.getInstance().provide(this);
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Class getServiceClass() throws ClassNotFoundException {
        return Class.forName(serviceInterface);
    }
}
