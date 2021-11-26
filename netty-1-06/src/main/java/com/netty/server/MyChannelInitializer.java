package com.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {

        // HttpResponse编码器
        channel.pipeline().addLast(new HttpResponseEncoder());

        // HttpRequest解码器
        channel.pipeline().addLast(new HttpRequestDecoder());

        // 消息入站处理器
        channel.pipeline().addLast(new MyServerHandler());
    }
}
