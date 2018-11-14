package com.tory.mysocketserver;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    final static int PORT = 9999;
    final static String PC_LOCAL_HOST = "10.0.2.2";
    final static String LOCAL_HOST = "127.0.0.1";

    final static String SEND_UDP = "send udp from Android";
    final static String SEND_TCP = "send tcp from Android";

    private boolean mQuitUdpRcv = false;
    private boolean mQuitTcpRcv = false;

    private List<Socket> mList = new ArrayList<Socket>();
    private ServerSocket server = null;
    private ExecutorService mExecutorService = null;
    private String receiveMsg;
    private String sendMsg;


    class UdpListener {
        public void onRecevied(String data)
        {
            showRecvUdp(data);
        }
    }

    private static final String TAG_TCP = "tcp server";
    TextView mTextViewUdp;
    TextView mTextViewTcp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnSendUdp).setOnClickListener(this);
        findViewById(R.id.btnRcvUdp).setOnClickListener(this);
        findViewById(R.id.btnQuitRcvUdp).setOnClickListener(this);

        findViewById(R.id.btnSendTcp).setOnClickListener(this);
        findViewById(R.id.btnRcvTcp).setOnClickListener(this);
        findViewById(R.id.btnQuitRcvTcp).setOnClickListener(this);

        mTextViewTcp = findViewById(R.id.tvTcp);
        mTextViewTcp.setText("Before receive anything from tcp.");

        mTextViewUdp =findViewById(R.id.tvUdp);
        mTextViewUdp.setText("Before receive anything from udp.");

        try {

            server = new ServerSocket(PORT);
            mExecutorService = Executors.newCachedThreadPool();

            Log.d(TAG_TCP, "start server");
            Toast.makeText(MainActivity.this, "start server.", Toast.LENGTH_SHORT).show();

            Socket client = null;
            while (true) {
                client = server.accept();
                mList.add(client);
                Toast.makeText(MainActivity.this, "accept one socket.", Toast.LENGTH_SHORT).show();
                mExecutorService.execute(new Service(client));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Service implements Runnable {
        private Socket socket;
        private BufferedReader in = null;
        private PrintWriter printWriter = null;

        public Service(Socket socket) {
            this.socket = socket;
            try {
                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                printWriter.println("成功连接服务器" + "（服务器发送）");
                Log.d(TAG_TCP,"成功连接服务器");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if ((receiveMsg = in.readLine()) != null) {
                        Log.d(TAG_TCP,"receiveMsg:" + receiveMsg);
                        if (receiveMsg.equals("BYE!")) {
                            Log.d(TAG_TCP,"客户端请求断开连接");
                            printWriter.println("服务端断开连接" + "（服务器发送）");
                            mList.remove(socket);
                            in.close();
                            socket.close();
                            break;
                        } else {
                            sendMsg = "我已接收：" + receiveMsg + "（服务器发送）";
                            printWriter.println(sendMsg);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSendUdp:
                Toast.makeText(MainActivity.this, "send udp", Toast.LENGTH_SHORT).show();
                sendUdp();
                break;
            case R.id.btnRcvUdp:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recvUdp(new UdpListener());
                    }
                }).start();
                break;
            case R.id.btnSendTcp:
                break;
            case R.id.btnRcvTcp:
                break;

            case R.id.btnQuitRcvTcp:
                mQuitTcpRcv = true;
                break;

            case R.id.btnQuitRcvUdp:
                mQuitUdpRcv = true;
                break;
        }
    }

    private void showRecvUdp (final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                mTextViewUdp.setText(msg);
            }
        });
    }
    private void recvUdp(UdpListener listener) {
        byte[] msg = new byte[1024];
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            try{
                while(!mQuitUdpRcv) {
                    socket.receive(packet);

                    String data = new String(packet.getData()).trim();
                    if (data != null) listener.onRecevied(data);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendUdp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket datagramSocket = new DatagramSocket(PORT);
                    byte [] buf = SEND_UDP.getBytes();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(LOCAL_HOST), PORT);

                    datagramSocket.send(packet);

                    datagramSocket.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "send udp error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();


    }
}
