package com.example.demo.config;

import com.example.demo.fetchParse.FetchAndParse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:application.properties")
public class Config {

    //Weather api url and baseApiUrl are configurable in the application.properties file
    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${base.api.url}")
    private String baseApiUrl;

    /**
     * @return FetchAndParse Object
     */
    @Bean
    public FetchAndParse fetchAndParse() {
        return new FetchAndParse(weatherApiUrl, createRestTemplate(), baseApiUrl);
    }

    /**
     * @return RestTemplate Object
     */
    @Bean
    public RestTemplate createRestTemplate() {
        return new RestTemplate();
    }

}
