package com.example.demo.fetchParse;

import com.example.demo.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

@Service
@EnableScheduling
public class FetchAndParse {

    private final String apiUrl;
    private final RestTemplate restTemplate;

    private final String baseApiUrl;

    @Autowired
    public FetchAndParse(@Value("${weather.api.url}") String apiUrl, RestTemplate restTemplate, @Value("${base.api.url}") String baseApiUrl) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        this.baseApiUrl = baseApiUrl;
    }

    private String fetch() {
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        return response.getBody();
    }

    private Document parseXMLString(String xmlData) throws Exception {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlData.getBytes())) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(inputStream);
        }
    }

    @Scheduled(cron = "0 15 * * * *")
    public void parseStations() throws Exception {
        Document document = parseXMLString(fetch());

        Element observationsElement = (Element) document.getElementsByTagName("observations").item(0);

        String timestampValue = observationsElement.getAttribute("timestamp");

        NodeList nodeList = document.getElementsByTagName("station");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element station = (Element) nodeList.item(i);
            String stationName = station.getElementsByTagName("name").item(0).getTextContent();

            if (stationName.equals("Tallinn-Harku") ||
                    stationName.equals("Tartu-Tõravere") ||
                    stationName.equals("Pärnu")) {

                int wmocode = Integer.parseInt(station.getElementsByTagName("wmocode").item(0).getTextContent());
                double airtemperature = Double.parseDouble(station.getElementsByTagName("airtemperature").item(0).getTextContent());
                String phenomenon = station.getElementsByTagName("phenomenon").item(0).getTextContent();
                double windspeed = Double.parseDouble(station.getElementsByTagName("windspeed").item(0).getTextContent());

                addWeather(new Weather(null, airtemperature, wmocode, stationName, phenomenon, windspeed, Integer.parseInt(timestampValue)));
            }
        }
    }

    private void addWeather(Weather weather) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Weather> request = new HttpEntity<>(weather, headers);

        restTemplate.postForObject(baseApiUrl, request, Weather.class);
    }
}
