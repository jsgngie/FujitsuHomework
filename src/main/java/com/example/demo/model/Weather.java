package com.example.demo.model;

import org.springframework.data.annotation.Id;

public record Weather(@Id Integer id, double airTemperature, int wmoCode, String name, String phenomenon, double windspeed, long timestamp) { }