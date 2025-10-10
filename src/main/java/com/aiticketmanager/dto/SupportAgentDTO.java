package com.aiticketmanager.dto;

import java.util.List;

public record SupportAgentDTO(
        Long userId,
        String fname,
        String lname,
        String email,
        String expertise,
        List<TicketSummaryDTO> assignedTickets
) {}
