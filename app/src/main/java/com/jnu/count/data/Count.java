package com.jnu.count.data;

import java.io.Serializable;


public class Count implements Serializable {
    private String name;
    private String price;
    private int pictureId;
    private String wechat;
    private String inoutCome;


    public Count(String name,String price,int pictureId,String wechat,String inoutCome) {
        this.name=name;
        this.price=price;
        this.pictureId=pictureId;
        this.wechat=wechat;
        this.inoutCome=inoutCome;
    }

    public String getName() {
        return name;
    }

    public int getPictureId() {
        return pictureId;
    }

    public String getPrice() {
        return price;
    }

    public String getWechat() {
        return wechat;
    }

    public String getInoutCome() {
        return inoutCome;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public void setInoutCome(String inoutCome) {
        this.inoutCome = inoutCome;
    }

}
