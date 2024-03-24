package com.example.demo;

import com.example.demo.config.Config;
import com.example.demo.fetchParse.FetchAndParse;
import com.example.demo.model.Weather;
import com.example.demo.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FujitsuApplication {

	@Autowired
	private Config config;

	public static void main(String[] args) {
		SpringApplication.run(FujitsuApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(WeatherRepository repository) {
		return args -> {

			//fetches, parses, saves data to weather database.
			FetchAndParse fetcherParser = config.fetchAndParse();
			fetcherParser.parseStations();

		};
	}
}
