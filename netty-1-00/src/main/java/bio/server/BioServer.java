package bio.server;

import bio.BioServerHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer extends Thread{
    private static ServerSocket serverSocket = null;

    public static void main(String[] args) {
        BioServer bioServer = new BioServer();
        bioServer.start();
    }


    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(7397));

            System.out.println("netty-1-00 server start done.");

            while (true) {
                // 接收客户端的连接
                Socket socket = serverSocket.accept();
                // 处理客户端的业务
                BioServerHandler bioServerHandler = new BioServerHandler(socket);
                bioServerHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("serverSocket 关闭失败");
            }
        }
    }


}
