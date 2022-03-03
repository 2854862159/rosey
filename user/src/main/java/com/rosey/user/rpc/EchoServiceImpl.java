package com.rosey.user.rpc;

import com.rosey.cloud.rpc.service.RpcService;
import com.rosey.cloud.test.EchoService;

/**
 * ClassName: EchoServiceImpl <br/>
 * Description: <br/>
 * date: 2021/8/25 3:49 下午<br/>
 *
 * @author tooru<br />
 */
@RpcService
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String message) {
        return message;
    }
}
