package com.example.demo.controller;


import com.example.demo.logic.Calculator;
import com.example.demo.model.Weather;
import com.example.demo.repository.WeatherRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/weather")
public class WeatherController {
    private final WeatherRepository repository;

    private final Calculator calculator;

    /**
     * @param repository - repository interface
     * @param calculator - calculator object containing business logic
     *
     *  Controller responsible for endpoint actions
     */
    public WeatherController(WeatherRepository repository, Calculator calculator) {
        this.repository = repository;
        this.calculator = calculator;
    }

    @GetMapping
    public Iterable<Weather> findAll() {
        return repository.findAll();
    }

    /**
     * @param newWeather - weather object to be posted to the database
     * @return saves the weather object into the database
     */
    @PostMapping
    public Weather addWeather(@RequestBody Weather newWeather) {
        return repository.save(newWeather);
    }

    /**
     * @param name - name specified in the api endpoint call
     * @return finds the latest entry with the specified city name
     */
    @GetMapping("/{name}")
    public Weather findLatestWeatherByCity(@PathVariable String name) {
        if (name.toLowerCase().contains("tallinn")) {name = "Tallinn-Harku";}
        else if (name.toLowerCase().contains("tartu")) {name = "Tartu-Tõravere";}
        else if (name.toLowerCase().contains("pärnu")) {name = "Pärnu";}

        Optional<Weather> latestWeather = repository.findTopByNameOrderByTimestampDesc(name);

        return latestWeather.orElse(null);
    }

    /**
     * @param name - name specified in the api endpoint call
     * @param vehicleType - vehicle type specified in the api endpoint call
     * @return calculates the fee according to business logic
     */
    @GetMapping("/{name}/{vehicleType}")
    public String calculate(@PathVariable String name, @PathVariable String vehicleType) {
        Weather info = findLatestWeatherByCity(name);

        return calculator.calculate(info, vehicleType);
    }
}
