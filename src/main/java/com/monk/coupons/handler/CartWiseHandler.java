package com.monk.coupons.handler;

import com.monk.coupons.dto.ApplicableCouponDTO;
import com.monk.coupons.dto.CartDTO;
import com.monk.coupons.dto.CartItemDTO;
import com.monk.coupons.entity.Coupon;
import com.monk.coupons.entity.CouponType;
import org.springframework.stereotype.Component;

@Component
public class CartWiseHandler implements CouponHandler {
    @Override
    public CouponType supportedType() {
        return CouponType.CART_WISE;
    }

    @Override
    public boolean isApplicable(Coupon c, CartDTO cart) {
        if (c.getCartWiseDetails() == null) return false;
        double total = cart.getItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        return total >= c.getCartWiseDetails().getThreshold();
    }

    @Override
    public ApplicableCouponDTO evaluate(Coupon c, CartDTO cart) {
        double total = cart.getItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        double percent = c.getCartWiseDetails().getPercent();
        double discount = total * percent / 100.0;
        return new ApplicableCouponDTO(c.getId(), CouponType.CART_WISE, discount, percent + "% off on cart");
    }

    @Override
    public CartDTO apply(Coupon c, CartDTO cart) {
        double total = cart.getItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        double percent = c.getCartWiseDetails().getPercent();
        double totalDiscount = total * percent / 100.0;
        // Distribute proportional discount to items
        for (CartItemDTO it : cart.getItems()) {
            double itemTotal = it.getPrice() * it.getQuantity();
            double itemShare = (itemTotal / total) * totalDiscount;
            it.setTotalDiscount(round(itemShare));
            it.setDiscountedPrice(round(itemTotal - itemShare));
        }
        // optionally set cart totals on cart DTO
        return cart;
    }
    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}

