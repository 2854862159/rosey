package com.rosey.cloud.discovery;

import com.rosey.cloud.registry.Registry;
import com.rosey.cloud.registry.RoseyServiceRegistryAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.ConditionalOnBlockingDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: RoseyDiscoveryClientAutoConfiguration <br/>
 * Description: <br/>
 * date: 2021/8/21 3:27 下午<br/>
 *
 * @author tooru<br />
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnDiscoveryEnabled
@ConditionalOnBlockingDiscoveryEnabled
@ConditionalOnRoseyDiscoveryEnabled
@AutoConfigureBefore({ SimpleDiscoveryClientAutoConfiguration.class,
        CommonsClientAutoConfiguration.class })
@AutoConfigureAfter({RoseyDiscoveryAutoConfiguration.class, RoseyServiceRegistryAutoConfiguration.class})
public class RoseyDiscoveryClientAutoConfiguration {

    @Bean
    @ConditionalOnBean(Registry.class)
    public DiscoveryClient discoveryClient(Registry registry){
        return new RoseyDiscoveryClient(registry);
    }

}
