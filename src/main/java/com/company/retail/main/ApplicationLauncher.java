package com.company.retail.main;
/**
 * @author omkar
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.company.retail.db.ShopListHolder;
import com.company.retail.service.ShopLocatorService;
import com.company.retail.service.ShopLocatorServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication(scanBasePackages = "com.company.retail")
public class ApplicationLauncher {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationLauncher.class, args);
        System.out.println("Application has started successfully!! Hurrey!!");
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ShopListHolder shopListHolder() {
        return new ShopListHolder();
    }

    @Bean
    public ShopLocatorService shopLocatorService() {
        return new ShopLocatorServiceImpl();
    }
    
}
