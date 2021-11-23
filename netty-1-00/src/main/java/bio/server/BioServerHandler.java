package bio.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class BioServerHandler extends Thread{
    private Socket socket;

    public BioServerHandler(Socket socket) throws IOException {
        this.socket = socket;
        while (!socket.isConnected()) {
            break;
        }

        // 连接建立好时，打印日志
        channelActice();
    }

    public void bioServerHandler() throws IOException {

        /**
         * 接收并打印客户端传来的信息
         * */
        readHandler(socket);
    }

    private void writeAndFlush(String msg) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(msg.getBytes());
        outputStream.flush();
    }

    private void readHandler(Socket socket) throws IOException {
        byte[] readBuffer = new byte[1024];
        InputStream inputStream = socket.getInputStream();

        int length;
        while ((length = inputStream.read(readBuffer)) != -1) {
            String s = new String(readBuffer, 0, length);
            System.out.println("客户端发送来的信息[" + s + "]");

            // 回复客户端消息
            writeAndFlush("我已经收到了你发送的消息");
        }
    }

    @Override
    public void run() {
        try {
            bioServerHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void channelActice() throws IOException {
        System.out.println("当前服务端已和" + socket.getLocalAddress() + "建立连接");
        writeAndFlush("服务端已和你建立好连接，请发送消息");
    }
}
