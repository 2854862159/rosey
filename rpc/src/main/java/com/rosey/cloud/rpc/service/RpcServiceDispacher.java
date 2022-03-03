package com.rosey.cloud.rpc.service;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: RpcServiceDispacher <br/>
 * Description: <br/>
 * date: 2021/8/25 3:15 下午<br/>
 *
 * @author tooru<br />
 */
public class RpcServiceDispacher {

    private static Map<Class, RpcServiceConfig> classRpcServiceDispacherMap = new HashMap<>();

    public static void add(Class cls, RpcServiceConfig serviceConfig){
        classRpcServiceDispacherMap.put(cls, serviceConfig);
    }

    public static RpcServiceConfig get(Class cls){
        return classRpcServiceDispacherMap.get(cls);
    }
}
