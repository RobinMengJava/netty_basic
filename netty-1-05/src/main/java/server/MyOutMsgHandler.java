package server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class MyOutMsgHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        String str = "ChannelOutboundHandlerAdapter.read发来一条消息\r\n";
        ctx.writeAndFlush(str);
        super.read(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String str = "ChannelOutboundHandlerAdapter.write发来一条消息\r\n";
        ctx.writeAndFlush(str);
        super.write(ctx, msg, promise);
    }
}
