package com.rosey.cloud.rpc.service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: RpcServiceConfig <br/>
 * Description: <br/>
 * date: 2021/8/25 3:13 下午<br/>
 *
 * @author tooru<br />
 */
public class RpcServiceConfig<T> {

    private T ref;

    private Class<T> tClass;

    private Map<String, Method> methodMap = new HashMap<>();

    @PostConstruct
    public void init(){
        Method[] methods = tClass.getMethods();
        for (Method method : methods) {
            String param = Arrays.stream(method.getParameters()).map(p -> p.getType().getTypeName()).collect(Collectors.joining("#"));
            methodMap.put(method.getName() + "#" + param, method);
        }
        RpcServiceDispacher.add(tClass.getInterfaces()[0], this);
    }

    public Object invoke(String method, Object... args) throws InvocationTargetException, IllegalAccessException {
        String paramType = "";
        if(args != null && args.length > 0){
            paramType = Arrays.stream(args).map(arg -> arg.getClass().getTypeName()).collect(Collectors.joining("#"));
        }

        Object invoke = methodMap.get(method + "#" + paramType).invoke(ref, args);
        return invoke;
    }

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    public Class<T> gettClass() {
        return tClass;
    }

    public void settClass(Class<T> tClass) {
        this.tClass = tClass;
    }

    public Map<String, Method> getMethodMap() {
        return methodMap;
    }

    public void setMethodMap(Map<String, Method> methodMap) {
        this.methodMap = methodMap;
    }
}
