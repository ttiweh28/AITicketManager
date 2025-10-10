package com.aiticketmanager.service.impl;

import com.aiticketmanager.dto.TicketDTO;
import com.aiticketmanager.dto.TicketSummaryDTO;
import com.aiticketmanager.model.Ticket;
import com.aiticketmanager.model.enums.TicketPriority;
import com.aiticketmanager.model.enums.TicketStatus;
import com.aiticketmanager.repository.*;
import com.aiticketmanager.service.TicketService;
import com.aiticketmanager.util.DtoMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final SupportAgentRepository agentRepository;
    private final ManagerRepository managerRepository;
    private final AIServiceRepository aiServiceRepository;
    private final DtoMapper mapper;

    @Override
    public TicketDTO createTicket(TicketDTO dto) {
        log.info("Creating ticket for customer ID {}", dto.customerId());

        //  Prevent new ticket if customer has unresolved HIGH priority ticket
        boolean hasUnresolvedHighPriority = ticketRepository.findByCustomer_CustomerId(dto.customerId())
                .stream()
                .anyMatch(t -> t.getPriority() == TicketPriority.HIGH &&
                        t.getStatus() != TicketStatus.RESOLVED);

        if (hasUnresolvedHighPriority) {
            throw new IllegalStateException("Customer has an unresolved high priority ticket");
        }

        Ticket ticket = mapper.toEntity(dto);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setSubmissionDate(java.time.LocalDate.now());

        Ticket saved = ticketRepository.save(ticket);
        log.info("Ticket created successfully with ID {}", saved.getTicketId());

        return mapper.toTicketDTO(saved);
    }


    @Override
    public List<TicketSummaryDTO> getTicketsByAgent(Long agentId) {
        log.debug("Fetching tickets for agent ID {}", agentId);
        return ticketRepository.findByAgent_AgentId(agentId)
                .stream()
                .map(mapper::toTicketSummary)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketSummaryDTO> getTicketsByCustomer(Long customerId) {
        log.debug("Fetching tickets for customer ID {}", customerId);
        return ticketRepository.findByCustomer_CustomerId(customerId)
                .stream()
                .map(mapper::toTicketSummary)
                .collect(Collectors.toList());
    }

    @Override
    public TicketDTO updateTicketStatus(Long ticketId, String status) {
        log.info("Updating status of ticket ID {} to {}", ticketId, status);

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        try {
            TicketStatus newStatus = TicketStatus.valueOf(status.toUpperCase());
            ticket.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ticket status: " + status);
        }

        Ticket updated = ticketRepository.save(ticket);
        log.debug("Ticket ID {} status updated successfully", ticketId);

        return mapper.toTicketDTO(updated);
    }


    @Override
    public void deleteTicket(Long ticketId) {
        log.warn("Deleting ticket ID {}", ticketId);
        if (!ticketRepository.existsById(ticketId)) {
            throw new IllegalArgumentException("Ticket not found");
        }
        ticketRepository.deleteById(ticketId);
    }
}
