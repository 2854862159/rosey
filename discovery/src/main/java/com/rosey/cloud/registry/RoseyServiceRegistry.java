package com.rosey.cloud.registry;

import com.rosey.cloud.discovery.RoseyDiscoveryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * ClassName: RoseyServiceRegistry <br/>
 * Description: <br/>
 * date: 2021/8/20 10:27 下午<br/>
 *
 * @author tooru<br />
 */
public class RoseyServiceRegistry implements ServiceRegistry<Registration> {

    private static final Logger log = LoggerFactory.getLogger(RoseyServiceRegistry.class);

    private final RoseyDiscoveryProperties properties;

    private final Registry registry;

    public RoseyServiceRegistry(RoseyDiscoveryProperties roseyDiscoveryProperties, Registry registry) {
        this.properties = roseyDiscoveryProperties;
        this.registry = registry;
    }

    @Override
    public void register(Registration registration) {

        if (StringUtils.isEmpty(registration.getServiceId())) {
            log.warn("No service to register for nacos client...");
            return;
        }

        registry.registerInstance(properties.getNamespace(), registration.getServiceId(), registration.getHost(), registration.getPort(), registration.getMetadata());
    }

    @Override
    public void deregister(Registration registration) {
        registry.deregisterInstance(properties.getNamespace(), registration.getServiceId(), registration.getHost(), registration.getPort());
    }

    @Override
    public void close() {

    }

    @Override
    public void setStatus(Registration registration, String status) {
        if (!status.equalsIgnoreCase("UP") && !status.equalsIgnoreCase("DOWN")) {
            log.warn("can't support status {},please choose UP or DOWN", status);
            return;
        }

        Map<String, String> metadata = registration.getMetadata();
        metadata.put("status", status);
        registry.updateInstance(properties.getNamespace(), registration.getServiceId(), registration.getHost(), registration.getPort(), registration.getMetadata());
    }

    @Override
    public Object getStatus(Registration registration) {
        return registry.getStatus(registration.getServiceId());
    }
}
