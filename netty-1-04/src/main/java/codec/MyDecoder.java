package codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {

    // 数据包基础长度
    private final int BASE_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 基础长度不足，则认为是不合法的数据包
        if (byteBuf.readableBytes() < BASE_LENGTH) return;

        // 记录包头位置
        int beginIdx;

        while (true) {
            // 获取包头开始的index
            beginIdx = byteBuf.readerIndex();
            // 标记包头开始的index
            byteBuf.markReaderIndex();
            // 读到了协议开始标志，结束while循环
            if (byteBuf.readByte() == 0x02) break;

            // 未读到包头，略过一个字节
            // 每次略过一个字节去读取包头信息的开始标记
            byteBuf.resetReaderIndex();
            byteBuf.readByte();

            // 当略过一个字节之后，数据包的长度又变得不满足，此时应该结束，等待后面的数据到达
            if (byteBuf.readableBytes() < BASE_LENGTH) return;

            // 剩余长度不足可读取数量
            int readabelCount = byteBuf.readableBytes();
            if (readabelCount < 1) {
                byteBuf.readerIndex(beginIdx);
                return;
            }

            // 长度域占4字节，读取int
            ByteBuf buf = byteBuf.readBytes(1);
            String msgLengthStr = buf.toString(Charset.forName("GBK"));
            int msgLength = Integer.parseInt(msgLengthStr);

            // 剩余长度不足可读取数量，没有结尾标识
            readabelCount = byteBuf.readableBytes();
            if (readabelCount < msgLength + 1) {
                byteBuf.readerIndex(beginIdx);
                return;
            }

            ByteBuf msgContent = byteBuf.readBytes(msgLength);

            // 如果没有结尾标识，还原指针位置
            byte end = byteBuf.readByte();
            if (end != 0x03) {
                byteBuf.readerIndex(beginIdx);
                return;
            }

            list.add(msgContent.toString(Charset.forName("GBK")));
        }
    }
}
