package com.rosey.cloud.rpc.service.autoconfiguration;

import com.rosey.cloud.rpc.service.RpcServicePostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

/**
 * ClassName: RpcReferenceAutoConfiguredRegistrar <br/>
 * Description: <br/>
 * date: 2021/8/24 7:53 下午<br/>
 *
 * @author tooru<br />
 */
public class RpcServiceAutoConfiguredRegistrar implements ImportBeanDefinitionRegistrar {

    private static final Logger log = LoggerFactory.getLogger(RpcServiceAutoConfiguredRegistrar.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(EnableRpcService.class.getName()));
        String basePackages = attributes.getString("basePackage");

        BeanDefinitionBuilder builder = rootBeanDefinition(RpcServicePostProcessor.class);
        builder.addConstructorArgValue(basePackages);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }
}
