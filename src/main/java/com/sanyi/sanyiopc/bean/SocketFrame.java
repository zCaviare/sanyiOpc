package com.sanyi.sanyiopc.bean;

import java.io.Serializable;

public class SocketFrame implements Serializable {
    private Pumpdata[] pumpdata;
    private String type;

    public Pumpdata[] getPumpdata() {
        return pumpdata;
    }

    public void setPumpdata(Pumpdata[] pumpdata) {
        this.pumpdata = pumpdata;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
