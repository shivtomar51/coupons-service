package com.monk.coupons.service;

import com.monk.coupons.dto.ApplicableCouponDTO;
import com.monk.coupons.dto.CartDTO;
import com.monk.coupons.dto.CartItemDTO;
import com.monk.coupons.entity.Coupon;
import com.monk.coupons.handler.CouponHandler;
import com.monk.coupons.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponEngine couponEngine;

    public CouponService(CouponRepository couponRepository, CouponEngine couponEngine) {
        this.couponRepository = couponRepository;
        this.couponEngine = couponEngine;
    }

    // --- CRUD operations ---

    @Transactional
    public Coupon createCoupon(Coupon coupon) {
        // Basic validation: ensure type is set and active flag defaulted
        if (coupon.getType() == null) {
            throw new IllegalArgumentException("Coupon type is required");
        }
        if (coupon.getActive() == null) coupon.setActive(Boolean.TRUE);
        return couponRepository.save(coupon);
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon getCouponById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Coupon not found with id: " + id));
    }

    @Transactional
    public Coupon updateCoupon(Long id, Coupon updated) {
        return couponRepository.findById(id)
                .map(existing -> {
                    // Update allowed fields (you can fine-tune)
                    existing.setCode(updated.getCode());
                    existing.setType(updated.getType());
                    existing.setActive(updated.getActive());
                    existing.setStartAt(updated.getStartAt());
                    existing.setEndAt(updated.getEndAt());
                    existing.setCartWiseDetails(updated.getCartWiseDetails());
                    existing.setProductWiseDetails(updated.getProductWiseDetails());
                    existing.setBxgyDetails(updated.getBxgyDetails());
                    return couponRepository.save(existing);
                })
                .orElseThrow(() -> new NoSuchElementException("Coupon not found with id: " + id));
    }

    @Transactional
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new NoSuchElementException("Coupon not found with id: " + id);
        }
        couponRepository.deleteById(id);
    }

    // --- Applicability and apply logic ---

    /**
     * Return all active coupons that are applicable to the given cart and the discount computed for each.
     */
    public List<ApplicableCouponDTO> findApplicableCoupons(CartDTO cart) {
        List<Coupon> all = couponRepository.findAll();
        List<ApplicableCouponDTO> out = new ArrayList<>();

        for (Coupon coupon : all) {
            if (!isActiveNow(coupon)) continue;
            CouponHandler handler = couponEngine.handlerFor(coupon);
            if (handler == null) continue; // safety
            try {
                if (handler.isApplicable(coupon, cart)) {
                    ApplicableCouponDTO eval = handler.evaluate(coupon, cart);
                    out.add(eval);
                }
            } catch (Exception ex) {
                // swallow handler errors per-coupon to continue checking others; log if desired
                // logger.warn("Error evaluating coupon {}: {}", coupon.getId(), ex.getMessage());
            }
        }
        // sort by discount descending (most beneficial first)
        out.sort(Comparator.comparing(ApplicableCouponDTO::getDiscountAmount).reversed());
        return out;
    }

    /**
     * Apply the coupon (by id) to the cart and return a new CartDTO with item-level discounts filled.
     * <p>
     * This method will:
     * - verify coupon exists and is active & in-valid-date-range,
     * - get handler and check applicability,
     * - call handler.apply() which must mutate and return the updated CartDTO (or a new one).
     */
    @Transactional
    public CartDTO applyCoupon(Long couponId, CartDTO cart) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new NoSuchElementException("Coupon not found with id: " + couponId));

        if (!isActiveNow(coupon)) {
            throw new IllegalArgumentException("Coupon is not active or is expired");
        }

        CouponHandler handler = couponEngine.handlerFor(coupon);
        if (handler == null) {
            throw new IllegalStateException("No handler available for coupon type: " + coupon.getType());
        }

        if (!handler.isApplicable(coupon, cart)) {
            throw new IllegalArgumentException("Coupon is not applicable to the provided cart");
        }

        // Let handler compute/mark discounts on cart items and return updated cart
        CartDTO updated = handler.apply(coupon, deepCopyCart(cart)); // pass a defensive copy

        // compute and fill cart totals (optional helper)
        computeCartTotals(updated);

        return updated;
    }

    // --- Helper methods ---

    private boolean isActiveNow(Coupon c) {
        if (c.getActive() == null || !c.getActive()) return false;
        LocalDateTime now = LocalDateTime.now();
        if (c.getStartAt() != null && now.isBefore(c.getStartAt())) return false;
        if (c.getEndAt() != null && now.isAfter(c.getEndAt())) return false;
        return true;
    }

    private void computeCartTotals(CartDTO cart) {
        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
        double totalDiscount = cart.getItems().stream()
                .mapToDouble(i -> i.getTotalDiscount() == null ? 0.0 : i.getTotalDiscount())
                .sum();
        cart.setTotalPrice(round(total));
        cart.setTotalDiscount(round(totalDiscount));
        cart.setFinalPrice(round(total - totalDiscount));
    }

    private CartDTO deepCopyCart(CartDTO cart) {
        // Defensive copy to avoid changing caller's cart object accidentally.
        CartDTO copy = new CartDTO();
        List<CartItemDTO> items = new ArrayList<>();
        for (CartItemDTO it : cart.getItems()) {
            CartItemDTO c = new CartItemDTO();
            c.setProductId(it.getProductId());
            c.setQuantity(it.getQuantity());
            c.setPrice(it.getPrice());
            c.setTotalDiscount(0.0);
            c.setDiscountedPrice(it.getPrice() * it.getQuantity());
            items.add(c);
        }
        copy.setItems(items);
        return copy;
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
