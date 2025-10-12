package com.aiticketmanager.controller;

import com.aiticketmanager.model.Customer;
import com.aiticketmanager.model.Manager;
import com.aiticketmanager.model.SupportAgent;
import com.aiticketmanager.repository.CustomerRepository;
import com.aiticketmanager.repository.ManagerRepository;
import com.aiticketmanager.repository.SupportAgentRepository;
import com.aiticketmanager.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final CustomerRepository customerRepository;
    private final SupportAgentRepository supportAgentRepository;
    private final ManagerRepository managerRepository;

    // ===== CUSTOMER REGISTRATION =====
    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        return ResponseEntity.ok(Map.of("message", "Customer registered successfully"));
    }

    // ===== SUPPORT AGENT REGISTRATION =====
    @PostMapping("/register/agent")
    public ResponseEntity<?> registerAgent(@RequestBody SupportAgent agent) {
        agent.setPassword(passwordEncoder.encode(agent.getPassword()));
        supportAgentRepository.save(agent);
        return ResponseEntity.ok(Map.of("message", "Support agent registered successfully"));
    }

    // ===== MANAGER REGISTRATION =====
    @PostMapping("/register/manager")
    public ResponseEntity<?> registerManager(@RequestBody Manager manager) {
        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        managerRepository.save(manager);
        return ResponseEntity.ok(Map.of("message", "Manager registered successfully"));
    }

    // ===== LOGIN (ALL USERS) =====
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        String token = jwtService.generateToken(username, Map.of());
        return ResponseEntity.ok(Map.of("token", token));
    }
}

