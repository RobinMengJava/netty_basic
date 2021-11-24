package codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        // 获取用于转码的字符串
        String msg = o.toString();
        // 将字符串转换为byte数组
        byte[] bytes = msg.getBytes();

        // 新创建一个byte数组
        byte[] send = new byte[bytes.length + 2];
        // 将原来数组copy到新数组中
        System.arraycopy(bytes, 0, send, 1, bytes.length);

        // 新数组的开头加上开始的标识符
        send[0] = 0x02;
        // 新数组的末尾加上结束的标识符
        send[send.length - 1] = 0x03;

        byteBuf.writeInt(send.length);
        byteBuf.writeBytes(send);
    }
}
