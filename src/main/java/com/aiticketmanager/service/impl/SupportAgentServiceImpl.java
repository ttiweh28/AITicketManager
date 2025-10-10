package com.aiticketmanager.service.impl;

import com.aiticketmanager.dto.SupportAgentDTO;
import com.aiticketmanager.model.SupportAgent;
import com.aiticketmanager.model.Ticket;
import com.aiticketmanager.repository.SupportAgentRepository;
import com.aiticketmanager.repository.TicketRepository;
import com.aiticketmanager.service.SupportAgentService;
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
public class SupportAgentServiceImpl implements SupportAgentService {

    private final SupportAgentRepository agentRepository;
    private final TicketRepository ticketRepository;
    private final DtoMapper mapper;

    @Override
    public SupportAgentDTO assignTicket(Long agentId, Long ticketId) {
        log.info("Assigning ticket {} to agent {}", ticketId, agentId);

        SupportAgent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found"));

        // Rule: no more than 10 unresolved tickets
        long activeTickets = ticketRepository.findByAgent_AgentId(agentId)
                .stream()
                .filter(t -> t.getStatus() != com.aiticketmanager.model.enums.TicketStatus.RESOLVED)
                .count();

        if (activeTickets >= 10) {
            throw new IllegalStateException("Agent already has 10 unresolved tickets");
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        ticket.setAgent(agent);

        ticketRepository.save(ticket);
        return mapper.toAgentDTO(agent);
    }

    @Override
    public List<SupportAgentDTO> getAllAgents() {
        return agentRepository.findAll()
                .stream()
                .map(mapper::toAgentDTO)
                .collect(Collectors.toList());
    }
}
