package com.evcard.model;

/**
 * Created by longwu on 16/10/20.
 */
public class EvcardEntity implements Comparable<EvcardEntity>{

    private String shopName;
    private String vehicleNo;
    private String vehicleModelName;
    private Integer drivingRange;
    private Integer status;
    private String vin;
    private String shopSeq;


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleModelName() {
        return vehicleModelName;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getShopSeq() {
        return shopSeq;
    }

    public void setShopSeq(String shopSeq) {
        this.shopSeq = shopSeq;
    }

    public void setVehicleModelName(String vehicleModelName) {
        this.vehicleModelName = vehicleModelName;
    }

    public Integer getDrivingRange() {
        return drivingRange;
    }

    public void setDrivingRange(Integer drivingRange) {
        this.drivingRange = drivingRange;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EVCARD{" +
                "地点='" + shopName + '\'' +
                ", 车牌号='" + vehicleNo + '\'' +
                ", 车品牌='" + vehicleModelName + '\'' +
                ", 行驶距离='" + drivingRange + '\'' +
                ", 状态='" + (status == 1 ? "正常充电中..." : "没有充电") + '\'' +
                ", vin='" + vin + '\'' +
                ", shopSeq='" + shopSeq + '\'' +
                '}';
    }

    public int compareTo(EvcardEntity o) {
        return o.drivingRange - this.drivingRange;
    }
}
