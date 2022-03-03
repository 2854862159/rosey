package com.rosey.cloud.rpc.service.autoconfiguration;

import com.rosey.cloud.discovery.RoseyDiscoveryAutoConfiguration;
import com.rosey.cloud.discovery.RoseyDiscoveryProperties;
import com.rosey.cloud.registry.RoseyServiceRegistryAutoConfiguration;
import com.rosey.cloud.remoting.NettyTransporter;
import com.rosey.cloud.rpc.service.event.ServiceInstancePreRegisteredEvent;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Map;

/**
 * ClassName: RpcMetadataAutoConfiguration <br/>
 * Description: <br/>
 * date: 2021/8/27 7:05 下午<br/>
 *
 * @author tooru<br />
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({RoseyDiscoveryAutoConfiguration.class})
public class RpcMetadataAutoConfiguration {

    @Bean
    public RpcServiceRegistrationEventPublishingAspect publishingAspect(){
        return new RpcServiceRegistrationEventPublishingAspect();
    }

    @EventListener(ServiceInstancePreRegisteredEvent.class)
    public void serviceInstancePreRegisteredEvent(ServiceInstancePreRegisteredEvent event){
        Registration source = event.getSource();
        Map<String, String> metadata = source.getMetadata();
        metadata.put("rpc.host", source.getHost());
        int port = source.getPort() + 10000;
        metadata.put("rpc.port", String.valueOf(port));

        NettyTransporter.bind(source.getHost(), port);
    }


}
