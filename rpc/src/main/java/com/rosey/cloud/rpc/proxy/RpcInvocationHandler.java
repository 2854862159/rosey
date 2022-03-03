package com.rosey.cloud.rpc.proxy;

import com.rosey.cloud.remoting.NettyClient;
import com.rosey.cloud.remoting.NettyTransporter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * ClassName: RpcInvocationHandler <br/>
 * Description: <br/>
 * date: 2021/8/24 7:41 下午<br/>
 *
 * @author tooru<br />
 */
public class RpcInvocationHandler implements InvocationHandler {

    private NettyClient nettyClient;

    private Class targetClass;

    public RpcInvocationHandler(Class targetClass, String host, Integer port) {
        this.targetClass = targetClass;
        nettyClient = NettyTransporter.connect(host, port);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            if ("toString".equals(methodName)) {
                return targetClass.toString();
            } else if ("hashCode".equals(methodName)) {
                return targetClass.hashCode();
            }
        } else if (parameterTypes.length == 1 && "equals".equals(methodName)) {
            return targetClass.equals(args[0]);
        }

        Object req = nettyClient.req(targetClass, method.getName(), args);
        return req;
    }
}
