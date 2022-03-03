package com.rosey.cloud.discovery;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: RoseyDiscoveryAutoConfiguration <br/>
 * Description: <br/>
 * date: 2021/8/21 2:52 下午<br/>
 *
 * @author tooru<br />
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnDiscoveryEnabled
@ConditionalOnRoseyDiscoveryEnabled
public class RoseyDiscoveryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RoseyDiscoveryProperties roseyDiscoveryProperties(){
        return new RoseyDiscoveryProperties();
    }

}
