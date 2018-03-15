package com.curve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EnableAutoConfiguration
@SpringBootApplication
@EntityScan(
        basePackageClasses = {Application.class, Jsr310JpaConverters.class}
)
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}