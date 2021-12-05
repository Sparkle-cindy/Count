package com.jnu.count.data;

import java.io.Serializable;

public class Count implements Serializable {
    private double price;

    public Count(double price) {
        this.price=price;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
