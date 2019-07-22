package com.sanyi.sanyiopc.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class Pumpdata implements Serializable {
    private String pname;//wincc变量的泵的前缀

    private String name;//wincc变量的名称，后缀

    private String value;//wincc变量的值

    private String unit;//wincc变量的单位

    private String group;//wincc变量的组

    private String Id;//wincc变量的泵id

    private String pumpname;//wincc变量的泵的名称

    private String cname;//wincc变量的中文名称

    private String col;//变量所在列

    public Pumpdata(String name, String value, String pname) {
        this.name = name;
        this.value = value;
        this.pname = pname;
    }

    public Pumpdata() {
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPumpname() {
        return pumpname;
    }

    public void setPumpname(String pumpname) {
        this.pumpname = pumpname;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
