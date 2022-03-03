package com.rosey.cloud.rpc.service.event;

import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationEvent;

/**
 * ClassName: ServiceInstancePreRegisteredEvent <br/>
 * Description: <br/>
 * date: 2021/8/27 7:43 下午<br/>
 *
 * @author tooru<br />
 */
public class ServiceInstancePreRegisteredEvent extends ApplicationEvent {

    private final ServiceRegistry<Registration> registry;

    public ServiceInstancePreRegisteredEvent(ServiceRegistry<Registration> registry,
                                             Registration source) {
        super(source);
        this.registry = registry;
    }

    @Override
    public Registration getSource() {
        return (Registration) super.getSource();
    }

    public ServiceRegistry<Registration> getRegistry() {
        return registry;
    }

}
