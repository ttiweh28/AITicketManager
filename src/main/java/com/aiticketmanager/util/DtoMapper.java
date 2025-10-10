package com.aiticketmanager.util;

import com.aiticketmanager.dto.*;
import com.aiticketmanager.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    private final ModelMapper mapper = new ModelMapper();

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
}
