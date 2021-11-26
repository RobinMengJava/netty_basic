package client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {


        // 解码器

        // 基于换行符号
        channel.pipeline().addLast(new LineBasedFrameDecoder(1024));

        // 解码转String, 注意调整自己的额编码格式GBK、UTF-8
        channel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));

        // 编码转String
        channel.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));

        // 消息出站处理器，在client向server发送消息时会触发此处理器
        channel.pipeline().addLast(new MyOutMsgHandler());
        // 消息入站处理器，即server向client发送消息的处理器
        channel.pipeline().addLast(new MyInMsgHandler());
    }
}
