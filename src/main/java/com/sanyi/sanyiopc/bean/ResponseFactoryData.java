package com.sanyi.sanyiopc.bean;

public class ResponseFactoryData {
    private String code;
    private Data[] data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public Data[] getData() {
        return data;
    }
}
