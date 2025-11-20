package com.monk.coupons.dto;


import com.monk.coupons.entity.CartWiseDetails;
import com.monk.coupons.entity.ProductWiseDetails;
import com.monk.coupons.entity.BxGyDetails;
import com.monk.coupons.entity.CouponType;

public class CreateCouponRequest {

    private String code;
    private CouponType type;

    private CartWiseDetails cartWiseDetails;
    private ProductWiseDetails productWiseDetails;
    private BxGyDetails bxgyDetails;

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
