package com.rosey.cloud.remoting.codec.handler;

import com.alibaba.fastjson.JSON;
import com.rosey.cloud.remoting.NettyClient;
import com.rosey.cloud.remoting.codec.DefaultFuture;
import com.rosey.cloud.remoting.codec.type.MyResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * ClassName: ClientHandler <br/>
 * Description: <br/>
 * date: 2021/5/31 10:04 上午<br/>
 *
 * @author tooru<br />
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

    /**
     * 连接到服务器时触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接建立成功");
    }

    /**
     * 消息到来时触发
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyResponse myResponse = (MyResponse) msg;
        DefaultFuture.received(ctx.channel(), myResponse);
    }

    /**
     * 发生异常时触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}