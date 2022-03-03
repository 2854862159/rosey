package com.rosey.cloud.remoting.codec;

import com.alibaba.fastjson.JSON;
import com.rosey.cloud.remoting.codec.type.MyRequest;
import com.rosey.cloud.remoting.codec.type.MyResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * ClassName: RossaiCodec <br/>
 * Description: <br/>
 * date: 2021/8/11 5:42 下午<br/>
 *
 * @author tooru<br />
 */
public class ClientRossaiCodec implements Codec{

    protected static final int HEADER_LENGTH = 12;

    protected static final short MAGIC = (short) 0xdabb;

    @Override
    public void encode(Channel channel, ByteBuf buffer, Object message) throws IOException {
        MyRequest data = (MyRequest) message;
        int savedWriteIndex = buffer.writerIndex();
        byte[] header = new byte[HEADER_LENGTH];
        // set magic number.
        Bytes.short2bytes(MAGIC, header);
        Bytes.long2bytes(data.getReqId(), header, 2);
        buffer.writerIndex(savedWriteIndex + HEADER_LENGTH);

        String json = JSON.toJSONString(data);
        buffer.writeCharSequence(json, Charset.forName("utf-8"));
        int length = json.length();
        Bytes.int2bytes(length, header, 8);
        buffer.writerIndex(savedWriteIndex);
        buffer.writeBytes(header); // write header.
        buffer.writerIndex(savedWriteIndex + HEADER_LENGTH + length);
    }

    @Override
    public Object decode(Channel channel, ByteBuf buffer) throws IOException {
        int readable = buffer.readableBytes();
        byte[] header = new byte[Math.min(readable, HEADER_LENGTH)];
        buffer.readBytes(header);

        if (readable < HEADER_LENGTH) {
            return DecodeResult.NEED_MORE_INPUT;
        }

        //body 长度
        int len = Bytes.bytes2int(header, 8);
        int tt = len + HEADER_LENGTH;
        if (readable < tt) {
            return DecodeResult.NEED_MORE_INPUT;
        }

        ChannelBufferInputStream is = new ChannelBufferInputStream(buffer, len);
        try{
            String str = is.toStr();
            MyResponse data = JSON.parseObject(str, MyResponse.class);
            return data;
        }finally {
            if (is.available() > 0) {
                try {
                    if (is.available() > 0) {
                        is.skip(is.available());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
