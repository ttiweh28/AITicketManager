package com.aiticketmanager.config;

import com.aiticketmanager.model.*;
import com.aiticketmanager.model.enums.Role;
import com.aiticketmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final SupportAgentRepository agentRepository;
    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TicketRepository ticketRepository;

    @Override
    public void run(String... args) {

        // --- SEED MANAGERS ---
        if (managerRepository.count() == 0) {
            List<Manager> managers = List.of(
                    buildManager("Alice", "Johnson", "manager1", "manager1@ads.com", "555-111-1111", "TX"),
                    buildManager("David", "Smith", "manager2", "manager2@ads.com", "555-111-2222", "CA"),
                    buildManager("Grace", "Lee", "manager3", "manager3@ads.com", "555-111-3333", "NY"),
                    buildManager("Henry", "Adams", "manager4", "manager4@ads.com", "555-111-4444", "FL"),
                    buildManager("Irene", "Cooper", "manager5", "manager5@ads.com", "555-111-5555", "WA")
            );
            managerRepository.saveAll(managers);
            System.out.println("Seeded 5 Managers with addresses");
        }

        // --- SEED SUPPORT AGENTS ---
        if (agentRepository.count() == 0) {
            List<SupportAgent> agents = List.of(
                    buildAgent("Bob", "Williams", "agent1", "agent1@ads.com", "555-222-1111", "Billing", "TX"),
                    buildAgent("Emma", "Watson", "agent2", "agent2@ads.com", "555-222-2222", "Technical Support", "CA"),
                    buildAgent("Frank", "White", "agent3", "agent3@ads.com", "555-222-3333", "Login Issues", "NY"),
                    buildAgent("Nina", "Green", "agent4", "agent4@ads.com", "555-222-4444", "Network", "FL"),
                    buildAgent("Oscar", "Brown", "agent5", "agent5@ads.com", "555-222-5555", "Payment", "WA")
            );
            agentRepository.saveAll(agents);
            System.out.println("Seeded 5 Support Agents with addresses");
        }

        // Assign all agents to the first manager
        Manager defaultManager = managerRepository.findByUserName("manager1")
                .orElseGet(() -> managerRepository.findAll().get(0)); // fallback

        agentRepository.findAll().forEach(agent -> {
            if (agent.getManager() == null) {
                agent.setManager(defaultManager);
                agentRepository.save(agent);
            }
        });
        System.out.println("Linked all agents to default manager: " + defaultManager.getUserName());

        // --- SEED CUSTOMERS ---
        if (customerRepository.count() == 0) {
            List<Customer> customers = List.of(
                    buildCustomer("Charlie", "Brown", "customer1", "customer1@ads.com", "555-333-1111", "TX"),
                    buildCustomer("Lily", "Evans", "customer2", "customer2@ads.com", "555-333-2222", "CA"),
                    buildCustomer("Mike", "Jordan", "customer3", "customer3@ads.com", "555-333-3333", "NY"),
                    buildCustomer("Nancy", "Allen", "customer4", "customer4@ads.com", "555-333-4444", "FL"),
                    buildCustomer("Peter", "Parker", "customer5", "customer5@ads.com", "555-333-5555", "WA")
            );
            customerRepository.saveAll(customers);
            System.out.println("Seeded 5 Customers with addresses");
        }
        ticketRepository.findAll().forEach(ticket -> {
            if (ticket.getManager() == null) {
                ticket.setManager(defaultManager);
                ticketRepository.save(ticket);
            }
        });
        System.out.println("Linked all existing tickets to default manager");
    }



    private Manager buildManager(String fname, String lname, String username, String email, String phone, String state) {
        Manager manager = new Manager();
        manager.setFname(fname);
        manager.setLname(lname);
        manager.setUserName(username);
        manager.setEmail(email);
        manager.setPassword(passwordEncoder.encode(username + "123"));
        manager.setPhone(phone);
        manager.setRole(Role.MANAGER);
        manager.setAddress(buildAddress(state));
        return manager;
    }

    private SupportAgent buildAgent(String fname, String lname, String username, String email,
                                    String phone, String expertise, String state) {
        SupportAgent agent = new SupportAgent();
        agent.setFname(fname);
        agent.setLname(lname);
        agent.setUserName(username);
        agent.setEmail(email);
        agent.setPassword(passwordEncoder.encode(username + "123"));
        agent.setPhone(phone);
        agent.setExpertise(expertise);
        agent.setRole(Role.AGENT);
        agent.setAddress(buildAddress(state));
        return agent;
    }

    private Customer buildCustomer(String fname, String lname, String username, String email,
                                   String phone, String state) {
        Customer customer = new Customer();
        customer.setFname(fname);
        customer.setLname(lname);
        customer.setUserName(username);
        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(username + "123"));
        customer.setPhone(phone);
        customer.setRole(Role.CUSTOMER);
        customer.setAddress(buildAddress(state));
        return customer;
    }

    private Address buildAddress(String state) {
        Address address = new Address();
        address.setStreet("123 " + state + " Street");
        address.setZip("750" + (int)(Math.random() * 100));
        address.setState(state);
        return address;
    }
}
