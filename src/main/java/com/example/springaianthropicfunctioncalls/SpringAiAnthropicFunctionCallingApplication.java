package com.example.springaianthropicfunctioncalls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WeatherConfigProperties.class)
public class SpringAiAnthropicFunctionCallingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiAnthropicFunctionCallingApplication.class, args);
    }

}
