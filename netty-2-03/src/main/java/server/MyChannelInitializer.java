package server;

import codec.ObjDecoder;
import codec.ObjEncoder;
import domain.MsgInfo;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {

        // protobuf处理
       channel.pipeline().addLast(new ObjDecoder(MsgInfo.class));
       channel.pipeline().addLast(new ObjEncoder(MsgInfo.class));

        // 在管道中添加自己接收数据的实现方法
        channel.pipeline().addLast(new MyServerHandler());
    }
}
