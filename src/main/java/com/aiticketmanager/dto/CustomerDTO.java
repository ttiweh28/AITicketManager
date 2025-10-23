package com.aiticketmanager.dto;



import java.util.List;

public record CustomerDTO(
        Long userId,
        String fname,
        String lname,
        String userName,
        String email,
        String phone,
        List<TicketSummaryDTO> tickets

) {}
