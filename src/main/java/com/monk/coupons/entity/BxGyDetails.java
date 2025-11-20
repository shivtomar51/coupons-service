package com.monk.coupons.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

import java.util.List;

@Embeddable
public class BxGyDetails {
    private Integer buyCount;          // X
    @ElementCollection
    private List<Long> buyProductIds;  // buy set
    private Integer getCount;          // Y
    @ElementCollection
    private List<Long> getProductIds;  // get set
    private Integer repetitionLimit;   // max times coupon can apply

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public List<Long> getBuyProductIds() {
        return buyProductIds;
    }

    public void setBuyProductIds(List<Long> buyProductIds) {
        this.buyProductIds = buyProductIds;
    }

    public Integer getGetCount() {
        return getCount;
    }

    public void setGetCount(Integer getCount) {
        this.getCount = getCount;
    }

    public List<Long> getGetProductIds() {
        return getProductIds;
    }

    public void setGetProductIds(List<Long> getProductIds) {
        this.getProductIds = getProductIds;
    }

    public Integer getRepetitionLimit() {
        return repetitionLimit;
    }

    public void setRepetitionLimit(Integer repetitionLimit) {
        this.repetitionLimit = repetitionLimit;
    }
}
