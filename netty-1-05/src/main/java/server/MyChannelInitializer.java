package server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // 解码器

        // 基于换行符的
        channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        // 自定义解码器
        channel.pipeline().addLast(new StringDecoder());

        // 自定义编码器
        channel.pipeline().addLast(new StringEncoder());

        // 在管道中添加我们自己的接收数据实现方法
        channel.pipeline().addLast(new MyServerHandler());
    }
}
