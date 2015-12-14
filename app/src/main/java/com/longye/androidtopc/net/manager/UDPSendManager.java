package com.longye.androidtopc.net.manager;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.longye.androidtopc.net.protocol.Click;
import com.longye.androidtopc.net.protocol.Connect;
import com.longye.androidtopc.net.protocol.ConnectFeedback;
import com.longye.androidtopc.net.protocol.ConnectedData;
import com.longye.androidtopc.net.protocol.MoveCursor;
import com.longye.androidtopc.net.protocol.Offline;
import com.longye.androidtopc.net.protocol.Online;
import com.longye.androidtopc.net.protocol.Protocol;
import com.longye.androidtopc.net.protocol.UnConnect;

import java.net.InetAddress;

public class UDPSendManager {
    public static final int SEND_PORT = 10250;

    public static void sendOnlineMessage(String ip, Context context) {
        try {
            boolean selfOnline = ip == null || ip.equals("255.255.255.255");
            InetAddress host = InetAddress.getByName(ip == null ? "255.255.255.255" : ip);
            Protocol p = new Protocol(host, UDPSendManager.SEND_PORT, new Online(Online.DEVICE_ANDROID,
                    selfOnline ? Online.SELF_ONLINE : Online.ONLINE_FEEDBACK, new Build().MODEL));
            p.send();
        } catch (Exception e) {
            sendFailed(context);
        }
    }

    public static void sendOfflineMessage(Context context) {
        try {
            InetAddress host = InetAddress.getByName("255.255.255.255");
            Protocol p = new Protocol(host, UDPSendManager.SEND_PORT, new Offline());
            p.send();
        } catch (Exception e) {
            sendFailed(context);
        }
    }

    public static void sendConnectRequest(String ip, Context context) {
        try {
            InetAddress host = InetAddress.getByName(ip);
            Protocol p = new Protocol(host, UDPSendManager.SEND_PORT,
                    new Connect(Online.DEVICE_ANDROID, Online.SELF_ONLINE, new Build().MODEL));
            p.send();
        } catch (Exception e) {
            sendFailed(context);
        }
    }

    public static void sendConnectFeedback(String ip, Context context) {
        try {
            InetAddress host = InetAddress.getByName(ip);
            Protocol p = new Protocol(host, UDPSendManager.SEND_PORT, new ConnectFeedback());
            p.send();
        } catch (Exception e) {
            sendFailed(context);
        }
    }

    public static void sendUnConnect(String ip, String password, Context context) {
        try {
            InetAddress host = InetAddress.getByName(ip);
            Protocol p = new Protocol(host, UDPSendManager.SEND_PORT, new UnConnect(password));
            p.send();
        } catch (Exception e) {
            sendFailed(context);
        }
    }

    public static void sendMoveCursor(String ip, String password, int x, int y, Context context) {
        try {
            InetAddress host = InetAddress.getByName(ip);
            Protocol p = new Protocol(host, UDPSendManager.SEND_PORT, new MoveCursor(password, x, y));
            p.send();
        } catch (Exception e) {
            sendFailed(context);
        }
    }

    public static void sendClick(String ip, String password, int button, int state, Context context) {
        try {
            InetAddress host = InetAddress.getByName(ip);
            Protocol p = new Protocol(host, UDPSendManager.SEND_PORT, new Click(password, button, state));
            p.send();
        } catch (Exception e) {
            sendFailed(context);
        }
    }

    private static void sendFailed(Context context) {
        Toast.makeText(context, "发送失败！", Toast.LENGTH_LONG).show();
    }

}
