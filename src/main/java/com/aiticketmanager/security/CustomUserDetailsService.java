package com.aiticketmanager.security;

import com.aiticketmanager.model.User;
import com.aiticketmanager.repository.CustomerRepository;
import com.aiticketmanager.repository.ManagerRepository;
import com.aiticketmanager.repository.SupportAgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final SupportAgentRepository supportAgentRepository;
    private final ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOpt = Stream.of(
                        customerRepository.findByUserName(username).map(u -> (User) u),
                        supportAgentRepository.findByUserName(username).map(u -> (User) u),
                        managerRepository.findByUserName(username).map(u -> (User) u)
                ).filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();

        User user = userOpt.orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}