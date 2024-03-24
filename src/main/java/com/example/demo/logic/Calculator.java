package com.example.demo.logic;

import com.example.demo.model.Weather;
import org.springframework.stereotype.Service;

@Service
public class Calculator {

    //Total delivery fee = RBF + ATEF + WSEF + WPEF

    /**
     *
     * Main method responsible for calculating the fee
     * @param info - weather data to be calculated on
     * @param vehicleType - vehicle type to be calculated on
     * @return double when calculation succeedes, or error string when illegal actions are seen.
     */
    public String calculate(Weather info, String vehicleType) {
        String name = info.name();
        double airTemp = info.airTemperature();
        double windSpeed = info.windspeed();
        String phenomenon = info.phenomenon();

        double rbf = getRBF(name, vehicleType);
        double atef = getATEF(airTemp, vehicleType);
        double wsef = getWSEF(windSpeed, vehicleType);
        double wpef = getWPEF(phenomenon, vehicleType);

        //catch, if conditions met are illegal.
        if (wsef == -1 || wpef == -1) return "Usage of selected vehicle type is forbidden";

        return String.valueOf(rbf + atef + wsef + wpef);
    }

    /**
     * @param city - city name
     * @param vehicleType - vehicle type
     * @return calculates the rbf according to business logic
     */
    private double getRBF(String city, String vehicleType) {
        return switch (city) {
            case "Tallinn-Harku" -> switch (vehicleType.toLowerCase()) {
                case "car" -> 4.0;
                case "scooter" -> 3.5;
                case "bike" -> 3.0;
                default -> 0.0; // Default value for unknown vehicle type
            };
            case "Tartu-Tõravere" -> switch (vehicleType.toLowerCase()) {
                case "car" -> 3.5;
                case "scooter" -> 3.0;
                case "bike" -> 2.5;
                default -> 0.0;
            };
            case "Pärnu" -> switch (vehicleType.toLowerCase()) {
                case "car" -> 3.0;
                case "scooter" -> 2.5;
                case "bike" -> 2.0;
                default -> 0.0;
            };
            default -> 0.0; // Default value for unknown city
        };
    }

    /**
     * @param airTemp - air temperature
     * @param vehicleType - vehicle type
     * @return calculates the atef according to business logic
     *
     */
    private double getATEF(double airTemp, String vehicleType) {
        if (vehicleType.equalsIgnoreCase("scooter") || vehicleType.equalsIgnoreCase("bike")) {
            if (-10 > airTemp) {return 1;
            }  else if (airTemp >= -10 && airTemp <= 0) {return 0.5;}
            else {return 0;}

        } else {return 0;}
    }

    /**
     * @param windSpeed - wind speed
     * @param vehicleType - vehicle type
     * @return calculates the wsef according to business logic
     * returns -1 when usage of vehicle is prohibited
     */
    private double getWSEF(double windSpeed, String vehicleType) {
        if (vehicleType.equalsIgnoreCase("bike")) {
            if (windSpeed >= 10 && windSpeed <= 20) {
                return 0.5;
            } else if (windSpeed > 20) {
                //throw error here
                return -1;
            }
        }
        return 0;
    }

    /**
     * @param phenomenon - weather phenomenon
     * @param vehicleType - vehicle type
     * @return calculates wpef according to business logic
     * returns -1 when usage of vehicle is prohibited
     */
    private double getWPEF(String phenomenon, String vehicleType) {
        if (vehicleType.equalsIgnoreCase("scooter") || vehicleType.equalsIgnoreCase("bike")) {
            if (phenomenon.toLowerCase().contains("glaze") || phenomenon.toLowerCase().contains("thunder") || phenomenon.toLowerCase().contains("hail")) {
                return -1;
                //throw error here
            }
            else if (phenomenon.toLowerCase().contains("snow") || phenomenon.toLowerCase().contains("sleet")) {
                return 1;
            } else if (phenomenon.toLowerCase().contains("rain") || phenomenon.toLowerCase().contains("shower")) {
                return 0.5;
            }
        }
        return 0;
    }
}
