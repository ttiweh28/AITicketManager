package com.aiticketmanager.config;

import com.aiticketmanager.model.*;
import com.aiticketmanager.model.enums.Role;
import com.aiticketmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final SupportAgentRepository agentRepository;
    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // --- SEED MANAGER ---
        if (managerRepository.count() == 0) {
            Manager manager = new Manager();
            manager.setFname("Alice");
            manager.setLname("Johnson");
            manager.setUserName("manager1");
            manager.setEmail("manager1@ads.com");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setPhone("555-111-1111");
            manager.setRole(Role.MANAGER);
            managerRepository.save(manager);
            System.out.println("✅ Seeded Manager: manager1 / manager123");
        }

        // --- SEED AGENT ---
        if (agentRepository.count() == 0) {
            SupportAgent agent = new SupportAgent();
            agent.setFname("Bob");
            agent.setLname("Williams");
            agent.setUserName("agent1");
            agent.setEmail("agent1@ads.com");
            agent.setPassword(passwordEncoder.encode("agent123"));
            agent.setPhone("555-222-2222");
            agent.setExpertise("Billing");
            agent.setRole(Role.AGENT);
            agentRepository.save(agent);
            System.out.println("✅ Seeded Agent: agent1 / agent123");
        }

        // --- SEED CUSTOMER ---
        if (customerRepository.count() == 0) {
            Customer customer = new Customer();
            customer.setFname("Charlie");
            customer.setLname("Brown");
            customer.setUserName("customer1");
            customer.setEmail("customer1@ads.com");
            customer.setPassword(passwordEncoder.encode("customer123"));
            customer.setPhone("555-333-3333");
            customer.setRole(Role.CUSTOMER);
            customerRepository.save(customer);
            System.out.println("✅ Seeded Customer: customer1 / customer123");
        }
    }
}
