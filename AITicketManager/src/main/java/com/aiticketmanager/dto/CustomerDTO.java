package com.aiticketmanager.dto;



import java.util.List;

public record CustomerDTO(
        Long userId,
        String fname,
        String lname,
        String email,
        List<TicketSummaryDTO> tickets
) {}
