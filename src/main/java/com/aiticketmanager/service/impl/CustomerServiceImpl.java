package com.aiticketmanager.service.impl;

import com.aiticketmanager.dto.CustomerDTO;
import com.aiticketmanager.model.Customer;
import com.aiticketmanager.repository.CustomerRepository;
import com.aiticketmanager.service.CustomerService;
import com.aiticketmanager.util.DtoMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final DtoMapper mapper;

    @Override
    public CustomerDTO createCustomer(CustomerDTO dto) {
        log.info("Creating new customer: {}", dto.email());
        Customer customer = mapper.toEntity(dto);
        Customer saved = customerRepository.save(customer);
        return mapper.toCustomerDTO(saved);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        log.debug("Fetching customer by ID {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        return mapper.toCustomerDTO(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(mapper::toCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        log.info("Updating customer ID {}", id);
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        existing.setEmail(dto.email());
        existing.setPhone(dto.phone());
        existing.setUserName(dto.userName());

        Customer updated = customerRepository.save(existing);
        return mapper.toCustomerDTO(updated);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer not found");
        }
        customerRepository.deleteById(id);
    }
}
