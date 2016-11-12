package com.evcard.model;

/**
 * Created by longwu on 16/11/11.
 */
public class ShopSeqEntity {

    private String shopSeq;
    private String shopName;
    private String address;

    public String getShopSeq() {
        return shopSeq;
    }

    public void setShopSeq(String shopSeq) {
        this.shopSeq = shopSeq;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return  "{shopSeq='" + shopSeq + '\'' +
                ", shopName='" + shopName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
