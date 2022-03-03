package com.rosey.cloud.remoting;

import com.rosey.cloud.remoting.codec.DefaultFuture;
import com.rosey.cloud.remoting.codec.handler.ClientDecodeHandler;
import com.rosey.cloud.remoting.codec.handler.ClientEncodeHandler;
import com.rosey.cloud.remoting.codec.handler.ClientHandler;
import com.rosey.cloud.remoting.codec.type.MyRequest;
import com.rosey.cloud.remoting.codec.type.MyResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * ClassName: NettyClient <br/>
 * Description: <br/>
 * date: 2021/8/22 7:13 下午<br/>
 *
 * @author tooru<br />
 */
public class NettyClient {

    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    private static final EventLoopGroup group = new NioEventLoopGroup();

    private static LongAdder longAdder = new LongAdder();

    private volatile Channel channel;

    public void connect(String host, Integer port) {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                // 自定义处理程序
                socketChannel.pipeline().addLast(new ClientDecodeHandler());
                socketChannel.pipeline().addLast(new ClientEncodeHandler());
                socketChannel.pipeline().addLast(new ClientHandler());
            }
        });
        // 绑定端口并同步等待
        ChannelFuture future = bootstrap.connect(host, port);

        boolean ret = future.awaitUninterruptibly(5000, MILLISECONDS);

        if (ret && future.isSuccess()) {
            this.channel = future.channel();
        }


//        finally {
//            group.shutdownGracefully();
//        }
    }

    public Object req(Class cls, String methodName, Object[] args) {
        MyRequest myRequest = new MyRequest();
        longAdder.add(1);
        myRequest.setReqId(longAdder.longValue());
        myRequest.setTarget(cls);
        myRequest.setMethodName(methodName);
        myRequest.setArgs(args);

        DefaultFuture defaultFuture = new DefaultFuture(channel, myRequest);
        try {
            defaultFuture.send();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MyResponse response = defaultFuture.getResponse();
        return response.getData();
    }
}
