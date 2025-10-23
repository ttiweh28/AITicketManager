package com.aiticketmanager.service;

import com.aiticketmanager.dto.TicketDTO;
import com.aiticketmanager.dto.TicketSummaryDTO;
import com.aiticketmanager.model.enums.TicketCategory;
import com.aiticketmanager.model.enums.TicketPriority;
import com.aiticketmanager.model.enums.TicketStatus;

import java.util.List;

public interface TicketService {

    TicketDTO createTicket(TicketDTO ticketDTO);

    TicketDTO getTicketById(Long ticketId);

    List<TicketSummaryDTO> getTicketsByAgent(Long agentId);

    List<TicketSummaryDTO> getTicketsByCustomer(Long customerId);

    TicketDTO updateTicketStatus(Long ticketId, String status);

    void deleteTicket(Long ticketId);

    List<TicketSummaryDTO> filterTicketsByAgent(Long agentId, TicketCategory category, TicketPriority priority, TicketStatus status);

}
