package com.monk.coupons.service;

import com.monk.coupons.dto.AgifyApiResponse;
import com.monk.coupons.entity.AgePrediction;
import com.monk.coupons.repository.AgePredictionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AgePredictionService {

    private final RestTemplate restTemplate;
    private final AgePredictionRepository repository;

    public AgePredictionService(RestTemplate restTemplate,
                                AgePredictionRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
    }
    public AgePrediction fetchAndSaveAge(String name) {
        String url = "https://api.agify.io?name={name}";
        AgifyApiResponse apiResponse =
                restTemplate.getForObject(url, AgifyApiResponse.class, name);

        if (apiResponse == null) {
            throw new RuntimeException("No response from Agify API");
        }

        AgePrediction prediction = new AgePrediction(
                apiResponse.getName(),
                apiResponse.getAge(),
                apiResponse.getCount()
        );

        return repository.save(prediction);
    }
}
