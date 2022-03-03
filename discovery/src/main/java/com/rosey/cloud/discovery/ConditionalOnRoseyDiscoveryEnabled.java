package com.rosey.cloud.discovery;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: ConditionalOnRoseyDiscoveryEnabled <br/>
 * Description: <br/>
 * date: 2021/8/21 2:52 下午<br/>
 *
 * @author tooru<br />
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@ConditionalOnProperty(value = "spring.cloud.rosey.discovery.enabled",
        matchIfMissing = true)
public @interface ConditionalOnRoseyDiscoveryEnabled {
}
