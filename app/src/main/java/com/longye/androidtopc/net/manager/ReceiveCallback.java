package com.longye.androidtopc.net.manager;

import com.longye.androidtopc.net.protocol.Protocol;

public interface ReceiveCallback {
    void onReceive(Protocol protocol);

    enum ReceiveType {
        Online, Offline, ConnectResponse, UnConnectResponse
    }
}


