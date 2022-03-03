package com.rosey.cloud.rpc.proxy.autoconfiguration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * ClassName: EnableRpcReferences <br/>
 * Description: <br/>
 * date: 2021/8/24 7:57 下午<br/>
 *
 * @author tooru<br />
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcReferenceAutoConfiguredRegistrar.class)
public @interface EnableRpcReferences {

    String basePackage();

}
