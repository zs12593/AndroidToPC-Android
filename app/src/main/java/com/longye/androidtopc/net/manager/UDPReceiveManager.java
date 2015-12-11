package com.longye.androidtopc.net.manager;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.longye.androidtopc.net.protocol.Protocol;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class UDPReceiveManager extends Thread {
    private static final int RECEIVE_SIZE = 512; // 每串数据长度,默认不超过
    public static final int RECEIVE_PORT = 10251;

    private Map<ReceiveCallback.ReceiveType, ReceiveCallback> mReceiveCallbacks;
    private Context mContext;
    private WifiManager.MulticastLock mLock;
    private boolean isActive = false;
    private DatagramSocket mSocket;
    private static UDPReceiveManager instance;

    private UDPReceiveManager(Context cxt) {
        mContext = cxt;
        mReceiveCallbacks = new HashMap<>();
    }

    public static UDPReceiveManager createInstance(Context cxt) {
        if (instance == null)
            instance = new UDPReceiveManager(cxt);

        return instance;
    }

    public static UDPReceiveManager getInstance() {
        return instance;
    }

    public static void release() {
        if (instance != null)
            instance.stopReceiveListen();
        instance = null;
    }

    public void addReceiveCallback(ReceiveCallback.ReceiveType type, ReceiveCallback receiveCallback) {
        mReceiveCallbacks.put(type, receiveCallback);
    }

    public void removeReceiveCallback(ReceiveCallback.ReceiveType type) {
        mReceiveCallbacks.remove(type);
    }

    public void startReceiveListen() {
        if (isActive || this.mLock != null) {
            return;
        }
        isActive = true;
        start();
    }

    public void stopReceiveListen() {
        isActive = false;
        // interrupt();
        if (mSocket != null && !mSocket.isClosed())
            mSocket.close();
        mSocket = null;
    }

    @Override
    public void run() {
        try {
            mSocket = new DatagramSocket(RECEIVE_PORT);
            byte[] data = new byte[RECEIVE_SIZE];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            WifiManager manager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            this.mLock = manager.createMulticastLock("UDPReceiveManager");
            this.mLock.acquire();
            while (isActive) {
                mSocket.receive(packet);
                Log.v("", "==== Received: " + new String(packet.getData(), 0, packet.getLength()));
                Protocol p = new Protocol(packet.getAddress(), packet.getPort(), new String(packet.getData(),
                        0, packet.getLength()));
                if (p.getParam() != null) {
                    for (Map.Entry entry : mReceiveCallbacks.entrySet()) {
                        if (entry.getKey().toString().equals(p.getParam().getProtocolName())) {
                            ReceiveCallback callback = (ReceiveCallback) entry.getValue();
                            callback.onReceive(p);
                        }
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            isActive = false;
            if (mSocket != null && !mSocket.isClosed())
                mSocket.close();
            mSocket = null;
            if (this.mLock != null)
                this.mLock.release();
            this.mLock = null;
        }
    }

}
