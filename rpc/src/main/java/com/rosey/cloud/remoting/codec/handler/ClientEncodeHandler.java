package com.rosey.cloud.remoting.codec.handler;

import com.rosey.cloud.remoting.codec.ClientRossaiCodec;
import com.rosey.cloud.remoting.codec.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * ClassName: ServerDecodeHandler <br/>
 * Description: <br/>
 * date: 2021/8/11 7:16 下午<br/>
 *
 * @author tooru<br />
 */
public class ClientEncodeHandler extends MessageToByteEncoder {

    private Codec codec = new ClientRossaiCodec();

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        System.out.println("client encode");
        codec.encode(ctx.channel(), out, msg);
    }
}
