package com.aiticketmanager.service.impl;

import com.aiticketmanager.dto.TicketDTO;
import com.aiticketmanager.dto.TicketSummaryDTO;
import com.aiticketmanager.integration.AIClient;
import com.aiticketmanager.model.SupportAgent;
import com.aiticketmanager.model.Ticket;
import com.aiticketmanager.model.enums.TicketCategory;
import com.aiticketmanager.model.enums.TicketPriority;
import com.aiticketmanager.model.enums.TicketStatus;
import com.aiticketmanager.repository.*;
import com.aiticketmanager.service.TicketService;
import com.aiticketmanager.util.DtoMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AIClient aiClient;

    @Override
    public TicketDTO createTicket(TicketDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();


        var customer = customerRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated customer not found"));

        log.info("Creating ticket for customer: {}", customer.getUserName());


        boolean hasUnresolvedHighPriority = ticketRepository.findByCustomer_UserId(customer.getUserId())
                .stream()
                .anyMatch(t -> t.getPriority() == TicketPriority.HIGH && t.getStatus() != TicketStatus.RESOLVED);

        if (hasUnresolvedHighPriority) {
            throw new IllegalStateException("Customer has an unresolved high-priority ticket");
        }


        Ticket ticket = mapper.toEntity(dto);


        ticket.setCustomer(customer);


        ticket.setStatus(TicketStatus.OPEN);
        ticket.setSubmissionDate(java.time.LocalDate.now());


        AIClient.ClassificationResult aiResult = aiClient.classifyTicket(dto.title(), dto.description());
        ticket.setCategory(aiResult.category());
        ticket.setPriority(aiResult.priority());


        Ticket saved = ticketRepository.save(ticket);
        log.info("Ticket created successfully with ID {}", saved.getTicketId());

        return mapper.toTicketDTO(saved);
    }


    @Override
    public List<TicketSummaryDTO> getTicketsByAgent(Long agentId) {
        log.debug("Fetching tickets for agent ID {}", agentId);
        return ticketRepository.findByAgent_UserId(agentId)
                .stream()
                .map(mapper::toTicketSummary)
                .collect(Collectors.toList());
    }
    @Override
    public TicketDTO getTicketById(Long ticketId) {
        log.debug("Fetching ticket by ID {}", ticketId);
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        return mapper.toTicketDTO(ticket);
    }


    @Override
    public List<TicketSummaryDTO> getTicketsByCustomer(Long customerId) {
        log.debug("Fetching tickets for customer ID {}", customerId);
        return ticketRepository.findByCustomer_UserId(customerId)
                .stream()
                .map(mapper::toTicketSummary)
                .collect(Collectors.toList());
    }

    @Override
    public TicketDTO updateTicketStatus(Long ticketId, String status) {
        log.info("Updating status of ticket ID {} to {}", ticketId, status);

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        // Step 2: Check if current user is an agent
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        log.debug("Authenticated user attempting status update: {}", username);

        // Load agent from repository (if logged-in user is an agent)
        SupportAgent agent = agentRepository.findByUserName(username).orElse(null);

        if (agent != null) {
            // Step 3: Enforce ownership — agent can only update their own tickets
            if (ticket.getAgent() == null || !ticket.getAgent().getUserId().equals(agent.getUserId())) {
                throw new SecurityException("Access denied: You cannot update tickets not assigned to you");
            }
        }

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

    @Override
    public List<TicketSummaryDTO> filterTicketsByAgent(Long agentId, TicketCategory category, TicketPriority priority, TicketStatus status) {
        log.debug("Filtering tickets for agent ID {} with filters: {}, {}, {}", agentId, category, priority, status);
        return ticketRepository.filterTicketsByAgent(agentId, category, priority, status)
                .stream()
                .map(mapper::toTicketSummary)
                .collect(Collectors.toList());
    }

}
