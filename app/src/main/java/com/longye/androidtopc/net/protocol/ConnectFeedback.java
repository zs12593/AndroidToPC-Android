package com.longye.androidtopc.net.protocol;


public class ConnectFeedback extends ProtocolParam {
    private boolean access;
    private String password;

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
