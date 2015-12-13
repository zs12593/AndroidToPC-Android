package com.longye.androidtopc.net.protocol;


public class Cursor extends ProtocolParam {

    private String password;

    public Cursor(String password) {
        super();
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
