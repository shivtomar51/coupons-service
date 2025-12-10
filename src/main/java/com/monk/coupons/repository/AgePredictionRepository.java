package com.monk.coupons.repository;

import com.monk.coupons.entity.AgePrediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgePredictionRepository extends JpaRepository<AgePrediction, Long> {
}