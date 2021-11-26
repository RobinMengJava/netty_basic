package com.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    public Logger logger = LoggerFactory.getLogger(MyServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "接收到消息：" + msg);

        String str = "服务端收到：" + new Date() + " " + msg + "\r\n";
        ctx.writeAndFlush(str);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        logger.info("链接报告开始...");
        logger.info("链接报告信息：有一客户端链接到本服务端");
        logger.info("链接报告IP：" + channel.localAddress().getHostString());
        logger.info("链接报告port: " + channel.localAddress().getPort());
        logger.info("链接报告完毕");

        // 通知客户端链接建立成功
        String str = "通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
        ctx.writeAndFlush(str);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端端断开链接" + ctx.channel().localAddress().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        logger.info("异常信息：\r\n" + cause.getMessage());
    }
}
