package com.company.retail.main;
/**
 * @author omkar
 * @Description Main class
 */

import java.net.URL;
import java.net.URLClassLoader;

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
//        printClassPath();
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
    
    public static void printClassPath() {
        //Get the System Classloader
        ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
        //Get the URLs
        URL[] urls = ((URLClassLoader)sysClassLoader).getURLs();
        for(int i=0; i< urls.length; i++)
            System.out.println(urls[i].getFile() + "\n");
    }
    
}
