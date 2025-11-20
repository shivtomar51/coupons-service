package com.monk.coupons.service;

import com.monk.coupons.entity.Coupon;
import com.monk.coupons.entity.CouponType;
import com.monk.coupons.handler.CouponHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CouponEngine {
    private final Map<CouponType, CouponHandler> map = new HashMap<>();

    @Autowired
    public CouponEngine(List<CouponHandler> handlers) {
        handlers.forEach(h -> map.put(h.supportedType(), h));
    }

    public CouponHandler handlerFor(Coupon coupon) {
        return map.get(coupon.getType());
    }
}
