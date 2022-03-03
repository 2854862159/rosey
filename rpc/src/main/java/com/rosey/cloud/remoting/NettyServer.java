package com.rosey.cloud.remoting;

import com.rosey.cloud.remoting.codec.handler.ServerDecodeHandler;
import com.rosey.cloud.remoting.codec.handler.ServerEncodeHandler;
import com.rosey.cloud.remoting.codec.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * ClassName: NettyServer <br/>
 * Description: <br/>
 * date: 2021/5/31 9:55 上午<br/>
 *
 * @author tooru<br />
 */
public class NettyServer {

    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    // 服务端NIO线程组
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workGroup = new NioEventLoopGroup();
    private volatile Channel channel;

    public ChannelFuture start(String host, Integer port) {
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 自定义服务处理
                            socketChannel.pipeline().addLast(new ServerDecodeHandler());
                            socketChannel.pipeline().addLast(new ServerEncodeHandler());
                            socketChannel.pipeline().addLast(new ServerHandler());
                        }
                    });
            // 绑定端口并同步等待
            channelFuture = bootstrap.bind(host, port);
            channelFuture.syncUninterruptibly();
            this.channel = channelFuture.channel();
            log.info("======Start Up Success!=========");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channelFuture;
    }

    public void close() {
        workGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("======Shutdown Netty Server Success!=========");
    }

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        ChannelFuture start = nettyServer.start("127.0.0.1", 7871);
        nettyServer.close();
    }

}