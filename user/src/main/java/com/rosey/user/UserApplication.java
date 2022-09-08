package com.rosey.user;

import com.rosey.cloud.rpc.proxy.autoconfiguration.EnableRpcReferences;
import com.rosey.cloud.rpc.service.RpcServiceConfig;
import com.rosey.cloud.rpc.service.RpcServiceDispacher;
import com.rosey.cloud.rpc.service.autoconfiguration.EnableRpcService;
import com.rosey.cloud.test.EchoService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * ClassName: UserApplication <br/>
 * Description: <br/>
 * date: 2021/8/21 3:53 下午<br/>
 *
 * @author tooru<br />
 */
@SpringBootApplication
@RestController
@EnableDiscoveryClient
@EnableRpcReferences(basePackage = "com.rosey.cloud")
@EnableRpcService(basePackage = "com.rosey.user.rpc")
public class UserApplication implements BeanFactoryAware, ApplicationContextAware {

    @PostMapping(value = "file")
    public void file(@RequestParam(value = "file") MultipartFile file, ImportPkgSenderDTO senderDTO) {
        System.out.println(senderDTO);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(UserApplication.class, args);
        String key = run.getEnvironment().getProperty("key");
        System.out.println(key);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("BeanFactory");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("ApplicationContext");
    }
}
