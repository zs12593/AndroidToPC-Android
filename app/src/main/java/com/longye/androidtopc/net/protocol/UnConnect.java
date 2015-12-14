package com.longye.androidtopc.net.protocol;

public class UnConnect extends ProtocolParam {
    private String password;

    public UnConnect(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
