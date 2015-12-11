package com.longye.androidtopc.net.protocol;


public class Cursor extends ProtocolParam {
    public static String CONNECTED_PASSWORD;

    private String password;

    public Cursor() {
        super();
        password = CONNECTED_PASSWORD;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
