package nio.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Set;

public class NioClientHandler extends Thread{
    private Selector selector;

    private Charset charset;

    public NioClientHandler(Selector selector, Charset charset) {
        this.selector = selector;
        this.charset = charset;
    }


    @Override
    public void run() {
        try {
            selector.select(1000);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            if (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                handleInput(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        // 处理输入
        if (!key.isValid()) return;

        Class<?> superclass = key.channel().getClass().getSuperclass();

        // 客户端socket
        if (superclass == SocketChannel.class) {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            // 客户端判断是否可以连接
            if (key.isConnectable()) {
                if (socketChannel.finishConnect()) {
                    // 连接建立好时，打印日志
                    channelActice(socketChannel);
                    // 注册一个可读事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                else {
                    System.exit(1);
                }
            }
        }

        if (superclass == ServerSocketChannel.class) {
            if (key.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
                channelActice(socketChannel);
            }

        }

        if (key.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            int readBytes = socketChannel.read(readBuffer);
            if (readBytes > 0) {
                readBuffer.flip();
                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);
                channelRead(socketChannel, new String(bytes, charset));
            }
            else if (readBytes < 0) {
                key.cancel();
                socketChannel.close();
            }
        }
    }

    /**
     * */
    private void channelRead(SocketChannel socketChannel, String message) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " 服务端收到客户端发送来的消息 [" + message.toString() + "]");
        writeAndFlush(socketChannel, "我已经收到你发送的消息 [" + message.toString() + "]");
    }

    /**
     * 与客户端建立连接时，打印日志
     * */
    private void channelActice(SocketChannel socketChannel) {
        try {
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " 当前服务端已和" + socketChannel.getLocalAddress() + "建立连接");
            writeAndFlush(socketChannel, "服务端已和你建立好连接，请发送消息");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给客户端推送消息
     * */
    private void writeAndFlush(SocketChannel socketChannel, String message) {
        try {
            byte[] bytes = message.toString().getBytes(charset);
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            socketChannel.write(writeBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
