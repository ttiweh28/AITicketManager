package com.aiticketmanager.service;

import com.aiticketmanager.dto.TicketDTO;
import com.aiticketmanager.dto.TicketSummaryDTO;

import java.util.List;

public interface TicketService {

    TicketDTO createTicket(TicketDTO ticketDTO);

    List<TicketSummaryDTO> getTicketsByAgent(Long agentId);

    List<TicketSummaryDTO> getTicketsByCustomer(Long customerId);

    TicketDTO updateTicketStatus(Long ticketId, String status);

    void deleteTicket(Long ticketId);
}
