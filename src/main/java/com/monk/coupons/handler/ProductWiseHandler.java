package com.monk.coupons.handler;

import com.monk.coupons.dto.ApplicableCouponDTO;
import com.monk.coupons.dto.CartDTO;
import com.monk.coupons.dto.CartItemDTO;
import com.monk.coupons.entity.Coupon;
import com.monk.coupons.entity.CouponType;
import org.springframework.stereotype.Component;

@Component
public class ProductWiseHandler implements CouponHandler {
    @Override
    public CouponType supportedType() {
        return CouponType.PRODUCT_WISE;
    }

    @Override
    public boolean isApplicable(Coupon c, CartDTO cart) {
        long pid = c.getProductWiseDetails().getProductId();
        return cart.getItems().stream().anyMatch(i -> i.getProductId().equals(pid));
    }

    @Override
    public ApplicableCouponDTO evaluate(Coupon c, CartDTO cart) {
        long pid = c.getProductWiseDetails().getProductId();
        double percent = c.getProductWiseDetails().getPercent();
        double discount = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(pid))
                .mapToDouble(i -> i.getPrice() * i.getQuantity() * percent / 100.0).sum();
        return new ApplicableCouponDTO(c.getId(), CouponType.PRODUCT_WISE, discount, percent + "% off product " + pid);
    }

    @Override
    public CartDTO apply(Coupon c, CartDTO cart) {
        // similar to evaluate but update items' totalDiscount and discounted price
        long pid = c.getProductWiseDetails().getProductId();
        double percent = c.getProductWiseDetails().getPercent();
        for (CartItemDTO it : cart.getItems()) {
            if (it.getProductId().equals(pid)) {
                double itemTotal = it.getPrice() * it.getQuantity();
                double itemDiscount = itemTotal * percent / 100.0;
                it.setTotalDiscount(round(itemDiscount));
                it.setDiscountedPrice(round(itemTotal - itemDiscount));
            } else {
                it.setTotalDiscount(0.0);
                it.setDiscountedPrice(round(it.getPrice() * it.getQuantity()));
            }
        }
        return cart;
    }
    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}

