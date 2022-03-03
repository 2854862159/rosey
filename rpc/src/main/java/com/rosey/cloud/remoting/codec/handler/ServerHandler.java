package com.rosey.cloud.remoting.codec.handler;

import com.alibaba.fastjson.JSON;
import com.rosey.cloud.remoting.codec.type.MyRequest;
import com.rosey.cloud.remoting.codec.type.MyResponse;
import com.rosey.cloud.rpc.service.RpcServiceConfig;
import com.rosey.cloud.rpc.service.RpcServiceDispacher;
import com.rosey.cloud.test.EchoService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ClassName: ServerHandler <br/>
 * Description: <br/>
 * date: 2021/5/31 9:57 上午<br/>
 *
 * @author tooru<br />
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 客户端数据到来时触发
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyRequest request = (MyRequest) msg;
        RpcServiceConfig rpcServiceConfig = RpcServiceDispacher.get(request.getTarget());
        Object result = rpcServiceConfig.invoke(request.getMethodName(), request.getArgs());

        MyResponse myResponse = new MyResponse();
        myResponse.setReqId(request.getReqId());
        myResponse.setData(result);

        ctx.write(myResponse);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将发送缓冲区的消息全部写到SocketChannel中
        ctx.flush();
    }

    /**
     * 发生异常时触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 释放与ChannelHandlerContext相关联的资源
        ctx.close();
    }
}