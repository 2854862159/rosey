package com.rosey.cloud.rpc.proxy.autoconfiguration;

import com.rosey.cloud.discovery.RoseyDiscoveryClient;
import com.rosey.cloud.rpc.proxy.RpcServiceScanner;
import com.rosey.cloud.rpc.service.autoconfiguration.EnableRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * ClassName: RpcReferenceAutoConfiguredRegistrar <br/>
 * Description: <br/>
 * date: 2021/8/24 7:53 下午<br/>
 *
 * @author tooru<br />
 */
public class RpcReferenceAutoConfiguredRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanClassLoaderAware, BeanFactoryAware {

    private static final Logger log = LoggerFactory.getLogger(RpcReferenceAutoConfiguredRegistrar.class);

    private BeanFactory beanFactory;

    private ResourceLoader resourceLoader;

    private ClassLoader classLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if (!AutoConfigurationPackages.has(this.beanFactory)) {
            log.debug("Could not determine auto-configuration package, automatic retrofit scanning disabled.");
        }else{
            log.debug("Searching for EventListener annotated with EventListener");
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                    importingClassMetadata.getAnnotationAttributes(EnableRpcReferences.class.getName()));
            String basePackages = attributes.getString("basePackage");

            List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
            packages.add(basePackages);
            RpcServiceScanner scanner = new RpcServiceScanner(registry, classLoader);

            if (this.resourceLoader != null) {
                scanner.setResourceLoader(this.resourceLoader);
            }

            scanner.registerFilters();
            scanner.scan(packages.stream().toArray(String[]::new));
        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
