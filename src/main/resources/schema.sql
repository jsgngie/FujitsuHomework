CREATE TABLE weather (
      id INT AUTO_INCREMENT PRIMARY KEY,
      air_temperature DOUBLE,
      wmo_code INT NOT NULL,
      name VARCHAR(255) NOT NULL,
      phenomenon VARCHAR(255),
      windspeed DOUBLE,
      timestamp BIGINT NOT NULL
);