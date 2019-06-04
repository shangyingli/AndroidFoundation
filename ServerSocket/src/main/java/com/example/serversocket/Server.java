package com.example.serversocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private static final int PORT = 9999;
    private ExecutorService executorService;
    Socket client;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        executorService = Executors.newFixedThreadPool(10);
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                System.out.println("服务器正在9999端口进行监听...");
                client = serverSocket.accept(); //监听客户端的连接， 无连接时阻塞
                System.out.println("客户端 : " + client.getInetAddress() + "成功连接到服务器");
                //有客户端连接, 读取客户端发来数据
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        while (client.isConnected() && !client.isClosed()) {
                            InputStream inputStream = null;
                            try {
                                System.out.println("等待客户端发消息");
                                inputStream = client.getInputStream();
                                InputStreamReader reader = new InputStreamReader(inputStream);
                                BufferedReader br = new BufferedReader(reader);
                                String msg = br.readLine();
                                System.out.println("客户端地址 ： " + client.getInetAddress() + "发来的消息 : " + msg);
                                //回发数据给客户端\
                                OutputStream outputStream = client.getOutputStream();
                                String send = "欢迎光临 ：" + client.getInetAddress() + "\n";
                                outputStream.write(send.getBytes("utf-8"));
                                outputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
