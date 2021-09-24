package ru.t1.asavin.techSupportAutomation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJms
@EnableCaching
@EnableScheduling
public class TechSupportAutomationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechSupportAutomationApplication.class, args);
    }
}
