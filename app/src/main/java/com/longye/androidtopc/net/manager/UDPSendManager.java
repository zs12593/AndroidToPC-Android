package com.longye.androidtopc.net.manager;

import android.os.Build;

import com.longye.androidtopc.net.protocol.Online;
import com.longye.androidtopc.net.protocol.Protocol;

import java.net.InetAddress;

public class UDPSendManager {
    public static final int SEND_PORT = 10250;

    public static void sendOnlineMessage(String ip) {
        try {
            boolean selfOnline = ip == null || ip.equals("255.255.255.255");
            InetAddress host = InetAddress.getByName(ip == null ? "255.255.255.255" : ip);
            Protocol p = new Protocol(host, UDPSendManager.SEND_PORT, new Online(Online.DEVICE_ANDROID,
                    selfOnline ? Online.SELF_ONLINE : Online.ONLINE_FEEDBACK, new Build().MODEL));
            p.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
