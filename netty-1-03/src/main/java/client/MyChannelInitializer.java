package client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

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
        channel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));

        // 在管道中添加我们自己接收数据的实现方法
        channel.pipeline().addLast(new NettyClientHandler());
    }
}
