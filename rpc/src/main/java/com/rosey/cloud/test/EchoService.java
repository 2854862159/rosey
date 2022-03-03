package com.rosey.cloud.test;

import com.rosey.cloud.rpc.proxy.RpcReference;

/**
 * ClassName: EchoService <br/>
 * Description: <br/>
 * date: 2021/8/25 3:49 下午<br/>
 *
 * @author tooru<br />
 */
@RpcReference(service = "user")
public interface EchoService {

    String echo(String message);

}
