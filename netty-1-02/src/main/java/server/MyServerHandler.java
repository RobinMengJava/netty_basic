package server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接收msg消息
//        ByteBuf buf = (ByteBuf) msg;
//
//        byte[] msgByte = new byte[buf.readableBytes()];
//        buf.readBytes(msgByte);
//        System.out.print(new Date() + "接收消息");
//        System.out.println(new String(msgByte, Charset.forName("GBK")));

        // 此处已经不需要自己进行解码
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "接收到消息：" + msg);


        // 通知客户端消息发送成功
        String str = "服务端收到：" + new Date() + " " + msg + "\r\n";
//        ByteBuf buf = Unpooled.buffer(str.getBytes().length);
//        buf.writeBytes(str.getBytes("GBK"));
//        ctx.writeAndFlush(buf);

        // 此处已不需要我们手动编码

        // 给全部链接的客户端发送此信息
        ChannelHandler.channelGroup.writeAndFlush(str);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接报告开始...");
        System.out.println("链接报告信息：有一客户端链接到本服务端");
        System.out.println("链接报告IP：" + channel.localAddress().getHostString());
        System.out.println("链接报告port: " + channel.localAddress().getPort());
        System.out.println("链接报告完毕");

        // 当有客户端连接时，添加到channelGroup通信组
        ChannelHandler.channelGroup.add(ctx.channel());

        // 通知客户端链接建立成功
        String str = "通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
//        ByteBuf buf = Unpooled.buffer(str.getBytes().length);
//        buf.writeBytes(str.getBytes("GBK"));
//        ctx.writeAndFlush(buf);

        // 此处已不需要我们手动编码
        ctx.writeAndFlush(str);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端端断开链接" + ctx.channel().localAddress().toString());

        // 客户端断开链接时
        ChannelHandler.channelGroup.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }
}
