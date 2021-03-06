package com.tory.mysockettest;

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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
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

    private ExecutorService mExecutorService = null;
    private String receiveMsg;

    class UdpListener {
        public void onRecevied(String data)
        {
            showRecvUdp(data);
        }
    }

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
        findViewById(R.id.btnConnect).setOnClickListener(this);
        findViewById(R.id.btnClose).setOnClickListener(this);

        mTextViewTcp = findViewById(R.id.tvTcp);
        mTextViewTcp.setText("Before receive anything from tcp.");

        mTextViewUdp =findViewById(R.id.tvUdp);
        mTextViewUdp.setText("Before receive anything from udp.");

        mExecutorService = Executors.newFixedThreadPool(5);

    }

    class SendHiThenClose implements Runnable {
        @Override
        public void run() {
            sendHiThenClose();
        }
    }

    private void sendHiThenClose() {
        try {
            Socket socket = new Socket(PC_LOCAL_HOST, 8888);
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream(), "UTF-8")), true);

            printWriter.println("hello, server");
            printWriter.close();
            socket.close();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(View view) {
        mExecutorService.execute(new  connectService());
    }
    public void send(View view) {
        String sendMsg = SEND_TCP.toString();
        mExecutorService.execute(new  sendService(sendMsg));
    }

    public void close(View view) {
        mExecutorService.execute(new sendService("BYE!"));
    }

    private PrintWriter printWriter;
    private static final String TAG_TCP = "tcp";
    private class sendService implements Runnable {
        private String msg;
        public sendService(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            Log.d(TAG_TCP, "sendService: " + msg);
            printWriter.println(this.msg);
        }
    }

    BufferedReader in;
    private class connectService implements Runnable {
        @Override
        public void run() {
            try {
                Socket socket = new Socket(LOCAL_HOST, PORT);
                socket.setSoTimeout(60000);
                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                receiveMsg();
            } catch (Exception e) {
                Log.e(TAG_TCP, ("connectService:" + e.getMessage()));
            }
        }
    }

    private void receiveMsg() {
        try {
            while (true) {
                if ((receiveMsg = in.readLine()) != null) {
                    Log.d(TAG_TCP, "receiveMsg:" + receiveMsg);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mTextViewTcp.setText(receiveMsg);
                        }
                    });
                }
            }
        } catch (IOException e) {
            Log.e(TAG_TCP, "receiveMsg: " + e.toString());
            e.printStackTrace();
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

            case R.id.btnQuitRcvUdp:
                mQuitUdpRcv = true;
                break;

            case R.id.btnSendTcp:
                //send(view);
                Toast.makeText(MainActivity.this, "Try to send tcp", Toast.LENGTH_SHORT).show();
                mExecutorService.execute(new SendHiThenClose());
                break;
            case R.id.btnConnect:
                connect(view);
                break;

            case R.id.btnClose:
                close(view);
                break;


        }
    }



    private void showRecvUdp (final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(PC_LOCAL_HOST), PORT);

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
