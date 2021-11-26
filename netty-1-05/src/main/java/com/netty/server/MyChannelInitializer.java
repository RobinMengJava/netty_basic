package com.netty.server;

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

        // 基于换行符
        channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        // 解码器
        channel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));

        // 编码器
        channel.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));

        // 消息入站处理器
        channel.pipeline().addLast(new MyServerHandler());
    }
}
