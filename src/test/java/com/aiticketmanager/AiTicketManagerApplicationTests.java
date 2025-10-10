package com.aiticketmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class AiTicketManagerApplicationTests {

    @Test
    void contextLoads() {
        try {
            SpringApplication.run(AiTicketManagerApplication.class);
            System.out.println("✅ Spring context loaded successfully!");
        } catch (Exception e) {
            System.out.println("❌ Context failed to load:");
            e.printStackTrace();
        }
    }
}
