package com.evcard.model;

/**
 * Created by longwu on 16/11/14.
 */
public class CancelEntity {
    private String illegalStatus;
    private String orderSeq;
    private String pickupStoreName;

    public String getIllegalStatus() {
        return illegalStatus;
    }

    public void setIllegalStatus(String illegalStatus) {
        this.illegalStatus = illegalStatus;
    }

    public String getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(String orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getPickupStoreName() {
        return pickupStoreName;
    }

    public void setPickupStoreName(String pickupStoreName) {
        this.pickupStoreName = pickupStoreName;
    }
}
