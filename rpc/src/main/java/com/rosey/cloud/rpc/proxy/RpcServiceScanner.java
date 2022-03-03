package com.rosey.cloud.rpc.proxy;

import com.rosey.cloud.discovery.RoseyDiscoveryClient;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.*;

/**
 * ClassName: OpenScanner <br/>
 * Description: <br/>
 * date: 2021/2/6 5:44 下午<br/>
 *
 * @author tooru<br />
 */
public class RpcServiceScanner extends ClassPathBeanDefinitionScanner {

    private final ClassLoader classLoader;

    public RpcServiceScanner(BeanDefinitionRegistry registry, ClassLoader classLoader) {
        super(registry, false);
        this.classLoader = classLoader;
    }

    public void registerFilters() {
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(RpcReference.class);
        this.addIncludeFilter(annotationTypeFilter);
    }

    @Override
    public boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        return super.checkCandidate(beanName, beanDefinition);
    }

    @Override
    protected boolean isCandidateComponent(
            AnnotatedBeanDefinition beanDefinition) {
        if (beanDefinition.getMetadata().isInterface()) {
            try {
                Class<?> target = ClassUtils.forName(
                        beanDefinition.getMetadata().getClassName(),
                        classLoader);
                return !target.isAnnotation();
            } catch (Exception ex) {
                logger.error("load class exception:", ex);
            }
        }
        return false;
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            logger.warn("No RpcReference was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            this.processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        Iterator var3 = beanDefinitions.iterator();

        while(var3.hasNext()) {
            BeanDefinitionHolder holder = (BeanDefinitionHolder)var3.next();
            ScannedGenericBeanDefinition definition = (ScannedGenericBeanDefinition)holder.getBeanDefinition();
            if (logger.isDebugEnabled()) {
                logger.debug("Creating RpcReference with name '" + holder.getBeanName() + "' and '" + definition.getBeanClassName() + "' Interface");
            }

            Map<String, Object> annotationAttributes = definition.getMetadata().getAnnotationAttributes("com.rosey.cloud.rpc.proxy.RpcReference");
            Object service = annotationAttributes.get("service");

            definition.getConstructorArgumentValues().addGenericArgumentValue(Objects.requireNonNull(definition.getBeanClassName()));
            definition.getConstructorArgumentValues().addGenericArgumentValue(Objects.requireNonNull(service));
            definition.setBeanClass(RpcFactoryBean.class);
        }

    }
}
