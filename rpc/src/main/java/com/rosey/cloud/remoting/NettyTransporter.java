package com.rosey.cloud.remoting;

/**
 * ClassName: Transporter <br/>
 * Description: <br/>
 * date: 2021/8/22 6:34 下午<br/>
 *
 * @author tooru<br />
 */
public class NettyTransporter {

    public static NettyServer bind(String host, Integer port){
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(host, port);

        return nettyServer;
    }

    public static NettyClient connect(String host, Integer port){
        NettyClient nettyClient = new NettyClient();
        nettyClient.connect(host, port);

        return nettyClient;
    }

}
