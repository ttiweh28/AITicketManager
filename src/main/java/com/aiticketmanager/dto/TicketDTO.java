package com.aiticketmanager.dto;

import com.aiticketmanager.model.enums.TicketCategory;
import com.aiticketmanager.model.enums.TicketPriority;
import com.aiticketmanager.model.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TicketDTO(
        Long ticketId,
        String title,
        String description,
        TicketCategory category,
        TicketPriority priority,
        TicketStatus status,
        LocalDate submissionDate,
        Long customerId,
        Long agentId,
        Long managerId,
        Long aiServiceId
) {}

