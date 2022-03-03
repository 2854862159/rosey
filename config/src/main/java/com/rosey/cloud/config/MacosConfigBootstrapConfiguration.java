package com.rosey.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: MacosConfigBootstrapConfiguration <br/>
 * Description: <br/>
 * date: 2021/7/15 10:28 下午<br/>
 *
 * @author tooru<br />
 */
@Configuration
public class MacosConfigBootstrapConfiguration {

    @Bean
    public MacosPropertySourceLocator macosPropertySourceLocator(){
        return new MacosPropertySourceLocator();
    }

}
