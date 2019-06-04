package com.example.picasodemo.bean;

import com.google.gson.annotations.SerializedName;

public class CatBean {

    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String imageUrl;
    @SerializedName("cuteLevel")
    private int cuteLevel;
    @SerializedName("desc")
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCuteLevel() {
        return cuteLevel;
    }

    public void setCuteLevel(int cuteLevel) {
        this.cuteLevel = cuteLevel;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ResponseCatBean{" + "name='" + name + '\'' +
                ",imageUrl='" + imageUrl + '\'' +
                ",cuteLevel='" + cuteLevel + '\'' +
                ",desc='" + desc + '\'';
    }
}
