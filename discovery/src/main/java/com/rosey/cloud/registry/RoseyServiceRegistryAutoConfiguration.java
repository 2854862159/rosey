package com.rosey.cloud.registry;

import com.rosey.cloud.discovery.ConditionalOnRoseyDiscoveryEnabled;
import com.rosey.cloud.discovery.RoseyDiscoveryAutoConfiguration;
import com.rosey.cloud.discovery.RoseyDiscoveryProperties;
import com.rosey.cloud.registry.file.FileServiceRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.serviceregistry.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: RoseyServiceRegistryAutoConfiguration <br/>
 * Description: <br/>
 * date: 2021/8/21 3:29 下午<br/>
 *
 * @author tooru<br />
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
@ConditionalOnRoseyDiscoveryEnabled
@ConditionalOnProperty(value = "spring.cloud.service-registry.auto-registration.enabled",
        matchIfMissing = true)
@AutoConfigureAfter({ AutoServiceRegistrationConfiguration.class,
        AutoServiceRegistrationAutoConfiguration.class,
        RoseyDiscoveryAutoConfiguration.class })
public class RoseyServiceRegistryAutoConfiguration {

    @Bean
    public Registry registry(){
        return new FileServiceRegistry();
    }

    @Bean
    @ConditionalOnBean(AutoServiceRegistrationProperties.class)
    public RoseyServiceRegistry roseyServiceRegistry(Registry registry, RoseyDiscoveryProperties roseyDiscoveryProperties){
        return new RoseyServiceRegistry(roseyDiscoveryProperties, registry);
    }

    @Bean
    @ConditionalOnBean(AutoServiceRegistrationProperties.class)
    public RoseyRegistration registration(RoseyDiscoveryProperties roseyDiscoveryProperties){
        return new RoseyRegistration(roseyDiscoveryProperties);
    }

    @Bean
    @ConditionalOnBean(AutoServiceRegistrationProperties.class)
    public RoseyAutoServiceRegistration roseyAutoServiceRegistration(RoseyServiceRegistry registry, RoseyRegistration registration, AutoServiceRegistrationProperties autoServiceRegistrationProperties){
        return new RoseyAutoServiceRegistration(registry, autoServiceRegistrationProperties, registration);
    }

}
