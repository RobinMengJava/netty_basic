package client;

import codec.ObjDecoder;
import codec.ObjEncoder;
import domain.MsgInfo;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import java.nio.charset.Charset;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // protebuf 处理
        channel.pipeline().addLast(new ObjDecoder(MsgInfo.class));
        channel.pipeline().addLast(new ObjEncoder(MsgInfo.class));

        // 在管道中添加我们自己的接收数据的实现方法
        channel.pipeline().addLast(new MyClientHandler());
    }
}
