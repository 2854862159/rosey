package com.rosey.cloud.discovery;

import com.rosey.cloud.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

/**
 * ClassName: RoseyDiscoveryClient <br/>
 * Description: <br/>
 * date: 2021/8/20 9:19 下午<br/>
 *
 * @author tooru<br />
 */
public class RoseyDiscoveryClient implements DiscoveryClient {

    private static final Logger log = LoggerFactory.getLogger(RoseyDiscoveryClient.class);

    public static final String DESCRIPTION = "Spring Cloud Rosey Discovery Client";

    private final Registry registry;

    public RoseyDiscoveryClient(Registry registry){
        this.registry = registry;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        return registry.getServiceInstance(serviceId);
    }

    @Override
    public List<String> getServices() {
        return registry.getService();
    }
}
