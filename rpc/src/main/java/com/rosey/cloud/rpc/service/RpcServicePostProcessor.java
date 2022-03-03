package com.rosey.cloud.rpc.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;
import static org.springframework.context.annotation.AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR;

/**
 * ClassName: RpcServicePostProcessor <br/>
 * Description: <br/>
 * date: 2021/8/24 8:32 下午<br/>
 *
 * @author tooru<br />
 */
public class RpcServicePostProcessor implements BeanDefinitionRegistryPostProcessor {

    private String packageName;

    public RpcServicePostProcessor(String packageName){
        this.packageName = packageName;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        if(!StringUtils.isEmpty(packageName)){
            RpcServiceComponentScanner scanner = new RpcServiceComponentScanner(beanDefinitionRegistry);
            scanner.registerFilters();
            scanner.scan(packageName);

            Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(packageName);
            Set<BeanDefinitionHolder> beanDefinitionHolders = new LinkedHashSet<>(beanDefinitions.size());

            SingletonBeanRegistry singletonBeanRegistry = SingletonBeanRegistry.class.cast(beanDefinitionRegistry);
            BeanNameGenerator beanNameGenerator = (BeanNameGenerator) singletonBeanRegistry.getSingleton(CONFIGURATION_BEAN_NAME_GENERATOR);

            if(beanNameGenerator == null){
                beanNameGenerator = new AnnotationBeanNameGenerator();
            }

            for (BeanDefinition beanDefinition : beanDefinitions) {

                String beanName = beanNameGenerator.generateBeanName(beanDefinition, beanDefinitionRegistry);
                BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
                beanDefinitionHolders.add(beanDefinitionHolder);
            }

            for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
                BeanDefinitionBuilder builder = rootBeanDefinition(RpcServiceConfig.class);
                builder.addPropertyReference("ref", beanDefinitionHolder.getBeanName());
                builder.addPropertyValue("tClass", beanDefinitionHolder.getBeanDefinition().getBeanClassName());

                beanDefinitionRegistry.registerBeanDefinition(beanDefinitionHolder.getBeanName()+"#Config", builder.getBeanDefinition());
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

}
