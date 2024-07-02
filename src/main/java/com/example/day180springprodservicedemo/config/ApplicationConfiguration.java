package com.example.day180springprodservicedemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // WE ARE TELLING THAT BEFORE MAKING ALL OTHER ANNOTATIONS LIKE REST CONTROLLER, SERVICE ETC - I WANT TO HAVE THIS CONFIGS BEANS. AND THEN OTHERS
public class ApplicationConfiguration {

    @Bean
    public RestTemplate getRestTemplate() {
        // By creating a method this way, incase if we want to configure some values like these when spring creates the bean/object, we can tell that inside this method like this
//        int numberOfRequests = 100;
//        int timeOut = 1;
//        boolean shouldAuthenticate = true;
        return new RestTemplate();
    }
}
