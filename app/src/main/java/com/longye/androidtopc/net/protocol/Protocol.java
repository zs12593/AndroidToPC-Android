package com.longye.androidtopc.net.protocol;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by LongYe on 2015/12/9.
 */
public class Protocol {

    protected ProtocolParam param;

    public Protocol(ProtocolParam p) {
        this.param = p;
    }

    public void send(InetAddress host, int port) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        String json = this.param.toString();
        byte[] data = json.getBytes();
        DatagramPacket p = new DatagramPacket(data, data.length, host, port);
        socket.send(p);
        socket.close();
    }

}
