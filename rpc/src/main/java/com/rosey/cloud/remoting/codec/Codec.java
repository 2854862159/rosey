package com.rosey.cloud.remoting.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.io.IOException;

/**
 * ClassName: Codec <br/>
 * Description: <br/>
 * date: 2021/8/11 5:39 下午<br/>
 *
 * @author tooru<br />
 */
public interface Codec {

    void encode(Channel channel, ByteBuf buffer, Object message) throws IOException;

    Object decode(Channel channel, ByteBuf buffer) throws IOException;


    enum DecodeResult {
        NEED_MORE_INPUT, SKIP_SOME_INPUT
    }

}
