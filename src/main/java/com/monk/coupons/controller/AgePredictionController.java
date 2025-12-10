package com.monk.coupons.controller;

import com.monk.coupons.entity.AgePrediction;
import com.monk.coupons.service.AgePredictionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/age")
public class AgePredictionController {

    private final AgePredictionService agePredictionService;

    public AgePredictionController(AgePredictionService agePredictionService) {
        this.agePredictionService = agePredictionService;
    }

    @GetMapping("/predict")
    public AgePrediction getAgeAndSave(@RequestParam String name) {
        return agePredictionService.fetchAndSaveAge(name);
    }
}
