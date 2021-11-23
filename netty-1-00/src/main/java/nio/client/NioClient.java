package nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class NioClient {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        boolean isConnect = socketChannel.connect(new InetSocketAddress("127.0.0.1", 7397));
        if (isConnect) {
            socketChannel.register(selector, SelectionKey.OP_READ);
        }
        else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
        System.out.println("netty-1-00 client start done...");

        new NioClientHandler(selector, Charset.forName("GBK")).start();
    }
}
