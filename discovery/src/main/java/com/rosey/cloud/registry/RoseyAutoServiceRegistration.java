package com.rosey.cloud.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.util.Assert;

/**
 * ClassName: RoseyAutoServiceRegistration <br/>
 * Description: <br/>
 * date: 2021/8/20 9:48 下午<br/>
 *
 * @author tooru<br />
 */
public class RoseyAutoServiceRegistration extends AbstractAutoServiceRegistration<Registration> {

    private static final Logger log = LoggerFactory
            .getLogger(RoseyAutoServiceRegistration.class);

    private RoseyRegistration registration;

    public RoseyAutoServiceRegistration(ServiceRegistry<Registration> serviceRegistry,
                                        AutoServiceRegistrationProperties autoServiceRegistrationProperties,
                                        RoseyRegistration registration) {
        super(serviceRegistry, autoServiceRegistrationProperties);
        this.registration = registration;
    }

    @Override
    protected void register() {
        if (!this.registration.getDiscoveryProperties().isRegisterEnabled()) {
            log.debug("Registration disabled.");
            return;
        }
        if (this.registration.getPort() < 0) {
            this.registration.setPort(getPort().get());
        }

        super.register();
    }

    @Override
    protected Object getConfiguration() {
        return registration.getDiscoveryProperties();
    }

    @Override
    protected boolean isEnabled() {
        return registration.getDiscoveryProperties().isRegisterEnabled();
    }

    @Override
    protected Registration getRegistration() {
        if (this.registration.getPort() < 0 && this.getPort().get() > 0) {
            this.registration.setPort(this.getPort().get());
        }
        Assert.isTrue(this.registration.getPort() > 0, "service.port has not been set");
        return this.registration;
    }

    @Override
    protected Registration getManagementRegistration() {
        return null;
    }
}
