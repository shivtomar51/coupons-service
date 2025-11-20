package com.monk.coupons.handler;

import com.monk.coupons.dto.ApplicableCouponDTO;
import com.monk.coupons.dto.CartDTO;
import com.monk.coupons.entity.Coupon;
import com.monk.coupons.entity.CouponType;

public interface CouponHandler {
    boolean isApplicable(Coupon coupon, CartDTO cart);

    ApplicableCouponDTO evaluate(Coupon coupon, CartDTO cart);

    CartDTO apply(Coupon coupon, CartDTO cart);

    CouponType supportedType();
}
