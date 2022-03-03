package com.rosey.cloud.remoting.codec.handler;

import com.rosey.cloud.remoting.codec.ClientRossaiCodec;
import com.rosey.cloud.remoting.codec.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;

/**
 * ClassName: ServerDecodeHandler <br/>
 * Description: <br/>
 * date: 2021/8/11 7:16 下午<br/>
 *
 * @author tooru<br />
 */
public class ClientDecodeHandler extends ByteToMessageDecoder {

    private Codec codec = new ClientRossaiCodec();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        do {
            int saveReaderIndex = in.readerIndex();
            Object msg = codec.decode(ctx.channel(), in);
            if (msg == Codec.DecodeResult.NEED_MORE_INPUT) {
                in.readerIndex(saveReaderIndex);
                break;
            } else {
                //is it possible to go here ?
                if (saveReaderIndex == in.readerIndex()) {
                    throw new IOException("Decode without read data.");
                }
                if (msg != null) {
                    out.add(msg);
                }
            }
        } while (in.isReadable());
    }
}
