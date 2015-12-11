package com.longye.androidtopc.net.protocol;


public class Online extends ProtocolParam {
    public static final int DEVICE_ANDROID = 0;
    public static final int DEVICE_PC = 1;

    public static final int SELF_ONLINE = 0;
    public static final int ONLINE_FEEDBACK = 1;

    private int deviceType; // 0 Android 1 PC
    private int onlineType; // SELF_ONLINE 主动上线  ONLINE_FEEDBACK 上线回文
    private String deviceName;

    public Online() {
        super();
    }

    public Online(int deviceType, int onlineType, String deviceName) {
        this();
        this.deviceType = deviceType;
        this.onlineType = onlineType;
        this.deviceName = deviceName;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getOnlineType() {
        return onlineType;
    }

    public void setOnlineType(int onlineType) {
        this.onlineType = onlineType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

}
