package com.sanyi.sanyiopc.bean;

public class Children {
    private String id;
    private String type;
    private String name;
    private String tag;
    private Children[] children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Children[] getChildren() {
        return children;
    }

    public void setChildren(Children[] children) {
        this.children = children;
    }
}
