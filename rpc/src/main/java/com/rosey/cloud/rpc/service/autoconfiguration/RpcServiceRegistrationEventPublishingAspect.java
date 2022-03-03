package com.rosey.cloud.rpc.service.autoconfiguration;

import com.rosey.cloud.rpc.service.event.ServiceInstancePreRegisteredEvent;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * ClassName: RpcServiceRegistrationEventPublishingAspect <br/>
 * Description: <br/>
 * date: 2021/8/27 7:36 下午<br/>
 *
 * @author tooru<br />
 */
@Aspect
public class RpcServiceRegistrationEventPublishingAspect implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    public static final String REGISTER_POINTCUT_EXPRESSION = "execution(* org.springframework.cloud.client.serviceregistry.ServiceRegistry.register(*)) && target(registry) && args(registration)";

    @Before(value = REGISTER_POINTCUT_EXPRESSION, argNames = "registry, registration")
    public void beforeRegister(ServiceRegistry registry, Registration registration) {
        applicationEventPublisher.publishEvent(
                new ServiceInstancePreRegisteredEvent(registry, registration));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
