package com.aiticketmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class AiTicketManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiTicketManagerApplication.class, args);
        System.out.println("AI Ticket Manager is running...");

    }
}
