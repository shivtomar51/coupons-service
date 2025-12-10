package com.monk.coupons.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Setter;

@Entity
public class AgePrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;
    @Setter
    private Integer age;
    @Setter
    private Integer count;

    public AgePrediction() {}

    public AgePrediction(String name, Integer age, Integer count) {
        this.name = name;
        this.age = age;
        this.count = count;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public Integer getAge() { return age; }

    public Integer getCount() { return count; }
}
