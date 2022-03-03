package com.rosey.cloud.rpc.proxy;

import com.rosey.cloud.discovery.RoseyDiscoveryClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * ClassName: RpcFactoryBean <br/>
 * Description: <br/>
 * date: 2021/8/24 7:28 下午<br/>
 *
 * @author tooru<br />
 */
public class RpcFactoryBean<T> implements FactoryBean<T> , BeanClassLoaderAware, ApplicationContextAware {

    private Class<T> targetClass;

    private ClassLoader classLoader;

    private String service;

    private DiscoveryClient discoveryClient;

    private ApplicationContext applicationContext;

    public RpcFactoryBean(Class<T> targetClass, String service) {
        this.targetClass = targetClass;
        this.service = service;
    }

    @Override
    public T getObject() throws Exception {
        List<ServiceInstance> instances = discoveryClient.getInstances(service);
        if(instances.isEmpty()){
            throw new RuntimeException("没有服务实例");
        }
        ServiceInstance serviceInstance = instances.get(0);
        Map<String, String> metadata = serviceInstance.getMetadata();
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{targetClass}, new RpcInvocationHandler(targetClass, metadata.get("rpc.host"), Integer.valueOf(metadata.get("rpc.port"))));
    }

    @Override
    public Class<?> getObjectType() {
        return targetClass;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.discoveryClient = this.applicationContext.getBean(DiscoveryClient.class);
    }
}
