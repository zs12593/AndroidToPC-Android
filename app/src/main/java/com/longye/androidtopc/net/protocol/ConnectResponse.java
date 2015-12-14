package com.longye.androidtopc.net.protocol;

public class ConnectResponse extends ProtocolParam {
    private boolean access;
    private String password;
    private String message;

    public ConnectResponse(boolean access, String password, String message) {
        this.access = access;
        this.password = password;
        this.message = message;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
