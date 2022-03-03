package com.rosey.cloud.rpc.service;

import com.rosey.cloud.rpc.proxy.RpcFactoryBean;
import com.rosey.cloud.rpc.proxy.RpcReference;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * ClassName: OpenScanner <br/>
 * Description: <br/>
 * date: 2021/2/6 5:44 下午<br/>
 *
 * @author tooru<br />
 */
public class RpcServiceComponentScanner extends ClassPathBeanDefinitionScanner {

    public RpcServiceComponentScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void registerFilters() {
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(RpcService.class);
        this.addIncludeFilter(annotationTypeFilter);
    }


    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        return beanDefinitions;
    }

    @Override
    public boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        return super.checkCandidate(beanName, beanDefinition);
    }
}
