package com.longye.androidtopc.net.protocol;

import com.google.gson.Gson;

public class ProtocolParam {
    protected long id;
    protected String protocolName;

    public ProtocolParam() {
        this.protocolName = this.getClass().getSimpleName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
