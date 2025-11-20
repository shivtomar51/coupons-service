package com.monk.coupons.controller;

import com.monk.coupons.dto.ApplicableCouponDTO;
import com.monk.coupons.dto.CartDTO;
import com.monk.coupons.entity.Coupon;
import com.monk.coupons.handler.CouponHandler;
import com.monk.coupons.repository.CouponRepository;
import com.monk.coupons.service.CouponEngine;
import com.monk.coupons.service.CouponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/coupons")
public class CouponController {
    private final CouponRepository repo;
    private final CouponEngine engine;
    private final CouponService couponService;

    public CouponController(CouponRepository repo, CouponEngine engine, CouponService couponService) {
        this.repo = repo;
        this.engine = engine;
        this.couponService = couponService;
    }

    @PostMapping
    public Coupon create(@RequestBody Coupon coupon) {
        return repo.save(coupon);
    }

    @GetMapping
    public List<Coupon> all() {
        return repo.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Coupon> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Coupon> update(@PathVariable Long id, @RequestBody Coupon input) {
        return repo.findById(id).map(c -> {
            input.setId(c.getId());
            return ResponseEntity.ok(repo.save(input));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Applicable coupons for a cart:
    @PostMapping("/applicable")
    public List<ApplicableCouponDTO> applicable(@RequestBody CartDTO cart) {
        List<Coupon> coupons = repo.findAll().stream().filter(Coupon::getActive).collect(Collectors.toList());
        List<ApplicableCouponDTO> out = new ArrayList<>();
        for (Coupon c : coupons) {
            CouponHandler h = engine.handlerFor(c);
            if (h != null && h.isApplicable(c, cart)) {
                out.add(h.evaluate(c, cart));
            }
        }
        return out;
    }

    // Apply a specific coupon
    @PostMapping("/apply/{id}")
    public ResponseEntity<?> apply(@PathVariable Long id, @RequestBody CartDTO cart) {
        try {
            CartDTO updated = couponService.applyCoupon(id, cart);
            return ResponseEntity.ok(updated);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
