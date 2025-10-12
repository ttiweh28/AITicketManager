package com.aiticketmanager.dto;

import java.util.List;

public record ManagerDTO(
        Long userId,
        String fname,
        String lname,
        String userName,
        String email,
        String phone,
        List<SupportAgentDTO> agents,
        List<TicketSummaryDTO> tickets
) {}

