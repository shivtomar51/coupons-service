package com.monk.coupons.dto;


import com.monk.coupons.entity.CouponType;

public class ApplicableCouponDTO {

    private Long couponId;
    private CouponType type;
    private Double discountAmount;
    private String description;

    public ApplicableCouponDTO() {}

    public ApplicableCouponDTO(Long couponId, CouponType type, Double discountAmount, String description) {
        this.couponId = couponId;
        this.type = type;
        this.discountAmount = discountAmount;
        this.description = description;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
