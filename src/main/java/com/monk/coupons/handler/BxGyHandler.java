package com.monk.coupons.handler;

import com.monk.coupons.dto.ApplicableCouponDTO;
import com.monk.coupons.dto.CartDTO;
import com.monk.coupons.dto.CartItemDTO;
import com.monk.coupons.entity.BxGyDetails;
import com.monk.coupons.entity.Coupon;
import com.monk.coupons.entity.CouponType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BxGyHandler implements CouponHandler {
    @Override
    public CouponType supportedType() {
        return CouponType.BXGY;
    }

    @Override
    public boolean isApplicable(Coupon c, CartDTO cart) {
        BxGyDetails d = c.getBxgyDetails();
        int totalBuyQty = cart.getItems().stream()
                .filter(i -> d.getBuyProductIds().contains(i.getProductId()))
                .mapToInt(CartItemDTO::getQuantity).sum();
        int totalGetQty = cart.getItems().stream()
                .filter(i -> d.getGetProductIds().contains(i.getProductId()))
                .mapToInt(CartItemDTO::getQuantity).sum();
        int timesByBuy = totalBuyQty / d.getBuyCount();
        int timesByGet = totalGetQty / d.getGetCount();
        return Math.min(timesByBuy, d.getRepetitionLimit()) > 0 && timesByGet > 0;
    }

    @Override
    public ApplicableCouponDTO evaluate(Coupon c, CartDTO cart) {
        BxGyDetails d = c.getBxgyDetails();
        int totalBuyQty = cart.getItems().stream()
                .filter(i -> d.getBuyProductIds().contains(i.getProductId()))
                .mapToInt(CartItemDTO::getQuantity).sum();
        int totalGetQty = cart.getItems().stream()
                .filter(i -> d.getGetProductIds().contains(i.getProductId()))
                .mapToInt(CartItemDTO::getQuantity).sum();
        int maxTimesByBuy = totalBuyQty / d.getBuyCount();
        int maxTimesByGet = totalGetQty / d.getGetCount();
        int times = Math.min(Math.min(maxTimesByBuy, d.getRepetitionLimit()), maxTimesByGet);

        // Choose cheapest get items to mark free
        List<CartItemDTO> eligibleGetItems = cart.getItems().stream()
                .filter(i -> d.getGetProductIds().contains(i.getProductId()))
                .collect(Collectors.toList());
        // Expand to per-unit list to pick cheapest
        List<Double> prices = new ArrayList<>();
        for (CartItemDTO it : eligibleGetItems) {
            for (int q = 0; q < it.getQuantity(); q++) prices.add(it.getPrice());
        }
        prices.sort(Double::compareTo);
        int totalFreeUnits = times * d.getGetCount();
        double discount = prices.stream().limit(totalFreeUnits).mapToDouble(Double::doubleValue).sum();
        return new ApplicableCouponDTO(c.getId(), CouponType.BXGY, discount, "BxGy: buy " + d.getBuyCount() + " get " + d.getGetCount());
    }

    @Override
    public CartDTO apply(Coupon c, CartDTO cart) {
        // Similar to evaluate but mark which units are free; for simplicity we apply discount to get-item totals
        ApplicableCouponDTO eval = evaluate(c, cart);
        double remainingToFree = eval.getDiscountAmount();
        // Apply discounts to get items (prefer cheapest)
        List<CartItemDTO> eligibleGetItems = cart.getItems().stream()
                .filter(i -> c.getBxgyDetails().getGetProductIds().contains(i.getProductId()))
                .sorted(Comparator.comparing(CartItemDTO::getPrice)) // cheapest first
                .collect(Collectors.toList());

        for (CartItemDTO it : eligibleGetItems) {
            double itemTotal = it.getPrice() * it.getQuantity();
            if (remainingToFree <= 0) break;
            double apply = Math.min(itemTotal, remainingToFree);
            it.setTotalDiscount(round(apply));
            it.setDiscountedPrice(round(itemTotal - apply));
            remainingToFree -= apply;
        }
        return cart;
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
