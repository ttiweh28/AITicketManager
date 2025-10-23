package com.aiticketmanager.util;

import com.aiticketmanager.dto.*;
import com.aiticketmanager.model.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
        return new TicketDTO(
                ticket.getTicketId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getCategory(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getSubmissionDate(),
                ticket.getCustomer() != null ? ticket.getCustomer().getUserId() : null,
                ticket.getAgent() != null ? ticket.getAgent().getUserId() : null,
                ticket.getManager() != null ? ticket.getManager().getUserId() : null,
                ticket.getAiService() != null ? ticket.getAiService().getAiServiceId() : null
        );
    }

    public TicketSummaryDTO toTicketSummary(Ticket ticket) {
        return new TicketSummaryDTO(
                ticket.getTicketId(),
                ticket.getTitle(),
                ticket.getPriority(),
                ticket.getStatus()
        );
    }

    public CustomerDTO toCustomerDTO(Customer c) {
        return new CustomerDTO(
                c.getUserId(),
                c.getFname(),
                c.getLname(),
                c.getUserName(),
                c.getEmail(),
                c.getPhone(),
                null
        );
    }

    public SupportAgentDTO toAgentDTO(SupportAgent agent) {
        return new SupportAgentDTO(
                agent.getUserId(),
                agent.getFname(),
                agent.getLname(),
                agent.getUserName(),
                agent.getEmail(),
                agent.getPhone(),
                agent.getExpertise(),
                agent.getTickets() != null
                        ? agent.getTickets().stream()
                        .map(this::toTicketSummary)
                        .collect(Collectors.toList())
                        : null
        );
    }
    public ManagerDTO toManagerDTO(Manager manager) {
        return new ManagerDTO(
                manager.getUserId(),
                manager.getFname(),
                manager.getLname(),
                manager.getUserName(),
                manager.getEmail(),
                manager.getPhone(),
                manager.getAgents() != null
                        ? manager.getAgents().stream()
                        .map(this::toAgentDTO)
                        .collect(Collectors.toList())
                        : null,
                manager.getTickets() != null
                        ? manager.getTickets().stream()
                        .map(this::toTicketSummary)
                        .collect(Collectors.toList())
                        : null
        );
    }


    // ===== DTO → ENTITY =====
    public Ticket toEntity(TicketDTO dto) {
        Ticket t = new Ticket();
        t.setTicketId(dto.ticketId());
        t.setTitle(dto.title());
        t.setDescription(dto.description());
        t.setPriority(dto.priority());
        t.setStatus(dto.status());
        t.setSubmissionDate(dto.submissionDate());
        return t;
    }


    public Customer toEntity(CustomerDTO dto) {
        Customer c = new Customer();
        c.setUserId(dto.userId());
        c.setFname(dto.fname());
        c.setLname(dto.lname());
        c.setUserName(dto.userName());
        c.setEmail(dto.email());
        c.setPhone(dto.phone());
        return c;
    }

    public SupportAgent toEntity(SupportAgentDTO dto) {
        SupportAgent a = new SupportAgent();
        a.setUserId(dto.userId());
        a.setFname(dto.fname());
        a.setLname(dto.lname());
        a.setUserName(dto.userName());
        a.setEmail(dto.email());
        a.setPhone(dto.phone());
        a.setExpertise(dto.expertise());
        return a;
    }

    public Manager toEntity(ManagerDTO dto) {
        Manager m = new Manager();
        m.setUserId(dto.userId());
        m.setFname(dto.fname());
        m.setLname(dto.lname());
        m.setUserName(dto.userName());
        m.setEmail(dto.email());
        m.setPhone(dto.phone());

        // Link subordinate agents and tickets if provided
        if (dto.agents() != null) {
            m.setAgents(dto.agents().stream()
                    .map(this::toEntity)
                    .collect(Collectors.toList()));
        }

        if (dto.tickets() != null) {
            m.setTickets(dto.tickets().stream()
                    .map(ts -> {
                        Ticket t = new Ticket();
                        t.setTicketId(ts.ticketId());
                        t.setTitle(ts.title());
                        t.setPriority(ts.priority());
                        t.setStatus(ts.status());
                        return t;
                    })
                    .collect(Collectors.toList()));
        }

        return m;
    }

}
