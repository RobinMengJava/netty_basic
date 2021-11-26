package com.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component("nettyServer")
public class NettyServer {

    private Logger logger = LoggerFactory.getLogger(NettyServer.class);

    // 配置服务端NIO线程组
    private final NioEventLoopGroup parentGroup = new NioEventLoopGroup();
    private final NioEventLoopGroup childGroup = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture bing(InetSocketAddress address) {
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new MyChannelInitializer());

            channelFuture = b.bind(address).syncUninterruptibly();
            logger.info("netty com.netty.server start done...");
            channel = channelFuture.channel();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            if (null != channelFuture && channelFuture.isSuccess()) {
                logger.info("============>>>>> netty com.netty.server started...");
            }
            else {
                logger.error("=============>>>>> netty com.netty.server start failed...");
            }
        }

        return channelFuture;

    }

    public void destory() {
        if (null == channel) return;
        channel.close();
        parentGroup.shutdownGracefully();
        childGroup.shutdownGracefully();
    }

    public Channel getChannel() {
        return channel;
    }
}
