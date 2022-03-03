package com.rosey.cloud.registry;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.Map;

/**
 * ClassName: Registry <br/>
 * Description: <br/>
 * date: 2021/8/21 1:45 下午<br/>
 *
 * @author tooru<br />
 */
public interface Registry {

    void registerInstance(String namespace, String service, String ip, Integer port);

    void registerInstance(String namespace, String service, String ip, Integer port, Map<String, String> metadata);

    void deregisterInstance(String namespace, String service, String ip, Integer port);

    void updateInstance(String namespace, String service, String ip, Integer port, Map<String, String> metadata);

    Object getStatus(String service);

    List<ServiceInstance> getServiceInstance(String service);

    List<ServiceInstance> getServiceInstance(String namespace, String service);

    List<String> getService();

    List<String> getService(String namespace);
}
