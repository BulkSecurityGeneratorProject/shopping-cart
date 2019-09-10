package com.sezer.shoppingcart.web.rest.vm;

public class CartDetailVM {
    private Double total;
    private Double campaignDiscount;
    private Double subTotal;
    private Double couponDiscount = 0d;
    private Double afterDiscount;
    private Double delivery;
    private Double finalTotal;

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getCampaignDiscount() {
        return campaignDiscount;
    }

    public void setCampaignDiscount(Double campaignDiscount) {
        this.campaignDiscount = campaignDiscount;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(Double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public Double getAfterDiscount() {
        return afterDiscount;
    }

    public void setAfterDiscount(Double afterDiscount) {
        this.afterDiscount = afterDiscount;
    }

    public Double getDelivery() {
        return delivery;
    }

    public void setDelivery(Double delivery) {
        this.delivery = delivery;
    }

    public Double getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(Double finalTotal) {
        this.finalTotal = finalTotal;
    }

    @Override
    public String toString() {
        return "CartDetailVM{" +
            "total=" + total +
            ", campaignDiscount=" + campaignDiscount +
            ", subTotal=" + subTotal +
            ", couponDiscount=" + couponDiscount +
            ", afterDiscount=" + afterDiscount +
            ", delivery=" + delivery +
            ", finalTotal=" + finalTotal +
            '}';
    }
}
