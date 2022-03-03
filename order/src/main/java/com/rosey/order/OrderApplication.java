package com.rosey.order;

import com.rosey.cloud.rpc.proxy.autoconfiguration.EnableRpcReferences;
import com.rosey.cloud.test.EchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: OrderApplication <br/>
 * Description: <br/>
 * date: 2021/8/26 6:07 下午<br/>
 *
 * @author tooru<br />
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RequestMapping("order")
@EnableRpcReferences(basePackage = "com.rosey.cloud")
public class OrderApplication {

    @Autowired
    private EchoService echoService;

    @RequestMapping("/test")
    public Object test(){
        String echo = echoService.echo("123131");
        return echo;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(OrderApplication.class, args);
    }

}
