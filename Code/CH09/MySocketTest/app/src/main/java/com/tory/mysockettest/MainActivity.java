package com.tory.mysockettest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    final static int PORT = 9999;
    final static String PC_LOCAL_HOST = "10.0.2.2";
    //final static String PC_LOCAL_HOST = "192.168.9.120";

    final static String SEND_UDP = "send udp from Android";
    final static String SEND_TCP = "send tcp from Android";

    private boolean mQuitUdpRcv = false;
    private boolean mQuitTcpRcv = false;

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
        findViewById(R.id.btnRcvTcp).setOnClickListener(this);
        findViewById(R.id.btnQuitRcvTcp).setOnClickListener(this);

        mTextViewTcp = findViewById(R.id.tvTcp);
        mTextViewTcp.setText("Before receive anything from tcp.");

        mTextViewUdp =findViewById(R.id.tvUdp);
        mTextViewUdp.setText("Before receive anything from udp.");

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
