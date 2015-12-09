package com.longye.androidtopc.net;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by LongYe on 2015/12/9.
 */
public class UDPManager implements Runnable {

    private static int RECV_SIZE = 1024; // 每串数据长度,默认不超过
    private int PC_PORT; // PC端接收端口
    private int ANDROID_PORT; // Android端接收端口

    private Context mContext;
    private WifiManager.MulticastLock mLock;
    private boolean isActive = false;

    public UDPManager(Context cxt) {
        this.mContext = cxt;
        WifiManager manager = (WifiManager) cxt
                .getSystemService(Context.WIFI_SERVICE);
        this.mLock = manager.createMulticastLock("UDPManager");
    }

    public void startRecvListen() {
        if (isActive) {
            return;
        }
        isActive = true;
        new Thread(this).start();
    }

    /**
     * 发送在线广播信息
     */
    public void sendOnlineMessage() {

    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(ANDROID_PORT);
            socket.setBroadcast(true);
            byte[] data = new byte[RECV_SIZE];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            this.mLock.acquire();
            while (isActive) {
                socket.receive(packet);
                String json = new String(packet.getData());
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            isActive = false;
            this.mLock.release();
            this.mLock = null;
        }

    }
}
