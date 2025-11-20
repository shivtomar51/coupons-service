package com.monk.coupons.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductWiseDetails {
    @Column(name = "product_product_id")
    private Long productId; // or you can support list as JSON
    @Column(name = "product_percent")
    private Double percent;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
}
