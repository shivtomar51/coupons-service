package com.monk.coupons.entity;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue
    private Long id;
    private String code;
    @Enumerated(EnumType.STRING)
    private CouponType type;
    private Boolean active = true;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    // common fields for cart/product/bxgy can be stored as JSON or embedded objects
    @Embedded
    private CartWiseDetails cartWiseDetails;
    @Embedded
    private ProductWiseDetails productWiseDetails;
    @Embedded
    private BxGyDetails bxgyDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public CartWiseDetails getCartWiseDetails() {
        return cartWiseDetails;
    }

    public void setCartWiseDetails(CartWiseDetails cartWiseDetails) {
        this.cartWiseDetails = cartWiseDetails;
    }

    public ProductWiseDetails getProductWiseDetails() {
        return productWiseDetails;
    }

    public void setProductWiseDetails(ProductWiseDetails productWiseDetails) {
        this.productWiseDetails = productWiseDetails;
    }

    public BxGyDetails getBxgyDetails() {
        return bxgyDetails;
    }

    public void setBxgyDetails(BxGyDetails bxgyDetails) {
        this.bxgyDetails = bxgyDetails;
    }
}

