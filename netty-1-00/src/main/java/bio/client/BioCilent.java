package bio.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class BioCilent {
    private static Socket socket = null;

    public static void main(String[] args) {
        try {
            socket = new Socket("127.0.0.1", 7397);

            /**
             * 获取用户在控制台上的输入
             * */
            String input = null;
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                input = scanner.nextLine();

                /**
                 * 给服务端发送消息
                 * */
                socket.getOutputStream().write(input.getBytes());

                /**
                 * 接收服务端发送的消息
                 * */
                byte[] readBuffer = new byte[1024];
                int length = socket.getInputStream().read(readBuffer);
                System.out.println("服务端发送来的消息 + [" + new String(readBuffer, 0, length) + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
