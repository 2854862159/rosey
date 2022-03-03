package com.rosey.cloud.registry;

import com.rosey.cloud.discovery.RoseyDiscoveryProperties;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.serviceregistry.Registration;

import java.net.URI;
import java.util.Map;

/**
 * ClassName: RoseyRegistration <br/>
 * Description: <br/>
 * date: 2021/8/20 9:34 下午<br/>
 *
 * @author tooru<br />
 */
public class RoseyRegistration implements Registration, ServiceInstance {

    private final RoseyDiscoveryProperties discoveryProperties;

    public RoseyRegistration(RoseyDiscoveryProperties roseyDiscoveryProperties){
        this.discoveryProperties = roseyDiscoveryProperties;
    }

    public RoseyDiscoveryProperties getDiscoveryProperties() {
        return discoveryProperties;
    }

    @Override
    public String getServiceId() {
        return discoveryProperties.getService();
    }

    @Override
    public String getHost() {
        return discoveryProperties.getHost();
    }

    @Override
    public int getPort() {
        return discoveryProperties.getPort();
    }

    @Override
    public boolean isSecure() {
        return discoveryProperties.isSecure();
    }

    @Override
    public URI getUri() {
        return DefaultServiceInstance.getUri(this);
    }

    @Override
    public Map<String, String> getMetadata() {
        return discoveryProperties.getMetadata();
    }

    public void setPort(int port) {
        this.discoveryProperties.setPort(port);
    }
}
