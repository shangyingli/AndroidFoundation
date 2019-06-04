package com.example.socketclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    private Button connect;
    private Button disConnect;
    private TextView receiveMessage;
    private Button receivedBtn;
    private EditText sendMessage;
    private Button sendBtn;

    private ExecutorService executorService;
    private Handler mainHandler;
    private Socket clientSocket;
    private static final int MSG_RECEIVE = 0x01;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect = findViewById(R.id.connect);
        disConnect = findViewById(R.id.disconnect);
        receiveMessage = findViewById(R.id.receive_message);
        receivedBtn = findViewById(R.id.Receive);
        sendMessage = findViewById(R.id.edit);
        sendBtn = findViewById(R.id.send);

        executorService = Executors.newFixedThreadPool(5);
        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_RECEIVE) {
                    receiveMessage.setText((String) msg.obj);
                }
            }
        };

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        //创建连接
                        try {
                            clientSocket = new Socket("169.254.29.48", 9999);
                            System.out.println(" 连接成功 ？ " + clientSocket.isConnected());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送消息给服务器
                final String msg = sendMessage.getText().toString();
                System.out.println("sendMessgae : " + msg);
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (clientSocket.isConnected()) {
                                OutputStream outputStream = clientSocket.getOutputStream();
                                outputStream.write((msg + "\n").getBytes("utf-8"));
                                outputStream.flush();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (clientSocket != null && clientSocket.isConnected() && !clientSocket.isClosed()) {
                        try {
                            System.out.println("等待服务器回消息");
                            InputStream inputStream = clientSocket.getInputStream();
                            InputStreamReader reader = new InputStreamReader(inputStream);
                            BufferedReader br = new BufferedReader(reader);
                            String response = br.readLine();
                            System.out.println("接收到服务器回的消息 : " + response);
                            Message message = mainHandler.obtainMessage();
                            message.what = MSG_RECEIVE;
                            message.obj = response;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }

}
