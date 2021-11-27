package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import util.MsgUtil;

public class NettyClient {

    public static void main(String[] args) {
        new NettyClient().connect("127.0.0.1", 7397);
    }
    private void connect(String inetHost, int inetPort) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.AUTO_READ, true)
                    .handler(new MyChannelInitializer());

            ChannelFuture f = b.connect(inetHost, inetPort).sync();
            System.out.println("netty client start done...");

            f.channel().writeAndFlush(MsgUtil.buildMsg(f.channel().id().toString(),"你好，使用protobuf通信格式的服务端list"));
            f.channel().writeAndFlush(MsgUtil.buildMsg(f.channel().id().toString(),"你好，使用protobuf通信格式的服务端list"));
            f.channel().writeAndFlush(MsgUtil.buildMsg(f.channel().id().toString(),"你好，使用protobuf通信格式的服务端list"));
            f.channel().writeAndFlush(MsgUtil.buildMsg(f.channel().id().toString(),"你好，使用protobuf通信格式的服务端list"));
            f.channel().writeAndFlush(MsgUtil.buildMsg(f.channel().id().toString(),"你好，使用protobuf通信格式的服务端list"));

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
