package com.sanyi.sanyiopc.bean;

public class Data {
private String token;
private String level;
private String user;
private String pwd;
private String id;
private String name;
private String type;
private String tag;
private Children[] children;
private Vib[] vib;

    public Vib[] getVib() {
        return vib;
    }

    public void setVib(Vib[] vib) {
        this.vib = vib;
    }


    public Children[] getChildren() {
        return children;
    }

    public void setChildren(Children[] children) {
        this.children = children;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
