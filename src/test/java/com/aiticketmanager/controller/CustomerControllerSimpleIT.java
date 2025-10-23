package com.aiticketmanager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerSimpleIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllCustomers_ShouldReturnOk() throws Exception {
        // when
        mockMvc.perform(get("/api/customers"))
                // then
                .andExpect(status().isOk())                         // checks HTTP 200
                .andExpect(content().contentType("application/json")) // checks content type
                .andExpect(jsonPath("$").isArray());                 //verifies it's a JSON array
    }
}
