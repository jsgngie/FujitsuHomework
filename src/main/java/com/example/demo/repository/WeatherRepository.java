package com.example.demo.repository;

import com.example.demo.model.Weather;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface WeatherRepository extends CrudRepository<Weather, Integer> {
    Optional<Weather> findTopByNameOrderByTimestampDesc(String name);
}
