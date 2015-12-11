package com.longye.androidtopc.net.protocol;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Protocol {

    private ProtocolParam param;
    private InetAddress host;
    private int port;
    private int state; // 0 可发送 1 不可发送

    public Protocol(InetAddress host, int port, ProtocolParam p) {
        this.host = host;
        this.port = port;
        this.param = p;
        this.state = 0;
    }

    public Protocol(InetAddress host, int port, String data) {
        this.param = createParam(data);
        this.host = host;
        this.port = port;
        this.state = 1;
    }

    public void send() {
        if (this.state == 1) return;

        new Thread() {
            @Override
            public void run() {
                DatagramSocket socket = null;
                try {
                    socket = new DatagramSocket();
                    String json = param.toString();
                    byte[] data = json.getBytes();
                    DatagramPacket p = new DatagramPacket(data, data.length, host, port);
                    socket.send(p);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null)
                        socket.close();
                }

            }
        }.start();
    }

    private ProtocolParam createParam(String data) {
        Gson gson = new Gson();
        ProtocolParam param = gson.fromJson(data, ProtocolParam.class);
        try {
            Class clz = Class.forName("com.longye.androidtopc.net.protocol." + param.protocolName);
            param = (ProtocolParam) gson.fromJson(data, clz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return param;
    }

    public ProtocolParam getParam() {
        return param;
    }

    public void setParam(ProtocolParam param) {
        this.param = param;
    }

    public InetAddress getHost() {
        return host;
    }

    public void setHost(InetAddress host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
