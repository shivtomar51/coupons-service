package com.monk.coupons.dto;


public class ApplyCouponResponse {

    private CartDTO updatedCart;
    private Long couponId;

    public ApplyCouponResponse() {}

    public ApplyCouponResponse(Long couponId, CartDTO updatedCart) {
        this.couponId = couponId;
        this.updatedCart = updatedCart;
    }

    public CartDTO getUpdatedCart() {
        return updatedCart;
    }

    public void setUpdatedCart(CartDTO updatedCart) {
        this.updatedCart = updatedCart;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }
}
