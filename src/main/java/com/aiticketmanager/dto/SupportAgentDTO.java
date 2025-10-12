package com.aiticketmanager.dto;

import java.util.List;

public record SupportAgentDTO(
        Long userId,
        String fname,
        String lname,
        String userName,
        String email,
        String phone,
        String expertise,
        List<TicketSummaryDTO> assignedTickets
) {}
