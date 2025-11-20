package com.monk.coupons.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CartWiseDetails {
    @Column(name = "cart_threshold")
    private Double threshold; // e.g., 100.0
    @Column(name = "cart_percent")
    private Double percent;   // e.g., 10.0 for 10%

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
}
