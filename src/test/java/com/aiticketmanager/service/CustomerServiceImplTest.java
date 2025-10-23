package com.aiticketmanager.service;

import com.aiticketmanager.dto.CustomerDTO;
import com.aiticketmanager.model.Customer;
import com.aiticketmanager.repository.CustomerRepository;
import com.aiticketmanager.service.impl.CustomerServiceImpl;
import com.aiticketmanager.util.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private DtoMapper mapper;
    @InjectMocks private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setUserId(1L);
        customer.setEmail("micheal@ads.com");
        customer.setUserName("micheal");
        customer.setPhone("555-333-4444");

        customerDTO = new CustomerDTO(1L, "micheal", "Ken", "micheal", "micheal@ads.com", "555-333-4444", null);
    }

    @Test
    void createCustomer_ShouldSaveSuccessfully() {
        when(mapper.toEntity(any(CustomerDTO.class))).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(mapper.toCustomerDTO(any(Customer.class))).thenReturn(customerDTO);

        CustomerDTO result = customerService.createCustomer(customerDTO);

        assertNotNull(result);
        assertEquals("micheal@ads.com", result.email());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void getCustomerById_ShouldThrowException_WhenNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> customerService.getCustomerById(99L));
    }
}
