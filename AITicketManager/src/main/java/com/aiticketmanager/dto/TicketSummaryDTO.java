package com.aiticketmanager.dto;

import com.aiticketmanager.model.enums.TicketPriority;
import com.aiticketmanager.model.enums.TicketStatus;

public record TicketSummaryDTO(
        Long ticketId,
        String title,
        TicketPriority priority,
        TicketStatus status
) {}

