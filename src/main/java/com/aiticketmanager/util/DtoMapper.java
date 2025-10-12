package com.aiticketmanager.util;

import com.aiticketmanager.dto.*;
import com.aiticketmanager.model.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    private final ModelMapper mapper;

    public DtoMapper() {
        this.mapper = new ModelMapper();
        this.mapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    // ===== ENTITY → DTO =====

    public TicketDTO toTicketDTO(Ticket ticket) {
        return mapper.map(ticket, TicketDTO.class);
    }

    public TicketSummaryDTO toTicketSummary(Ticket ticket) {
        return new TicketSummaryDTO(
                ticket.getTicketId(),
                ticket.getTitle(),
                ticket.getPriority(),
                ticket.getStatus()
        );
    }

    public CustomerDTO toCustomerDTO(Customer customer) {
        return mapper.map(customer, CustomerDTO.class);
    }

    public SupportAgentDTO toAgentDTO(SupportAgent agent) {
        return mapper.map(agent, SupportAgentDTO.class);
    }

    public ManagerDTO toManagerDTO(Manager manager) {
        return mapper.map(manager, ManagerDTO.class);
    }

    // ===== DTO → ENTITY =====

    public Ticket toEntity(TicketDTO dto) {
        return mapper.map(dto, Ticket.class);
    }

    public Customer toEntity(CustomerDTO dto) {
        return mapper.map(dto, Customer.class);
    }

    public SupportAgent toEntity(SupportAgentDTO dto) {
        return mapper.map(dto, SupportAgent.class);
    }

    public Manager toEntity(ManagerDTO dto) {
        return mapper.map(dto, Manager.class);
    }
}
