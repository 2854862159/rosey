package com.rosey.cloud.rpc.service;

import java.lang.annotation.*;

/**
 * ClassName: RpcReference <br/>
 * Description: <br/>
 * date: 2021/8/24 7:48 下午<br/>
 *
 * @author tooru<br />
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcService {
}
