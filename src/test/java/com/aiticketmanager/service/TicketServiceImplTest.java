package com.aiticketmanager.service;

import com.aiticketmanager.dto.TicketDTO;
import com.aiticketmanager.model.Ticket;
import com.aiticketmanager.model.enums.TicketCategory;
import com.aiticketmanager.model.enums.TicketPriority;
import com.aiticketmanager.model.enums.TicketStatus;
import com.aiticketmanager.repository.TicketRepository;
import com.aiticketmanager.repository.CustomerRepository;
import com.aiticketmanager.repository.SupportAgentRepository;
import com.aiticketmanager.repository.ManagerRepository;
import com.aiticketmanager.repository.AIServiceRepository;
import com.aiticketmanager.service.impl.TicketServiceImpl;
import com.aiticketmanager.util.DtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock private TicketRepository ticketRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private SupportAgentRepository agentRepository;
    @Mock private ManagerRepository managerRepository;
    @Mock private AIServiceRepository aiServiceRepository;
    @Mock private DtoMapper mapper;

    @InjectMocks private TicketServiceImpl ticketService;

    private Ticket ticket;
    private TicketDTO dto;

    @BeforeEach
    void setup() {
        ticket = new Ticket();
        ticket.setTicketId(1L);
        ticket.setPriority(TicketPriority.LOW);
        ticket.setStatus(TicketStatus.OPEN);

        dto = new TicketDTO(
                1L,
                "Test Ticket",
                "This is a test ticket",
                TicketCategory.OTHER,
                TicketPriority.LOW,
                TicketStatus.OPEN,
                LocalDate.now(),
                1L,
                null,
                null,
                null
        );

    }

    @Test
    void createTicket_ShouldThrowException_WhenHighPriorityUnresolvedExists() {
        Ticket existing = new Ticket();
        existing.setPriority(TicketPriority.HIGH);
        existing.setStatus(TicketStatus.OPEN);

        when(ticketRepository.findByCustomer_UserId(1L)).thenReturn(List.of(existing));

        assertThrows(IllegalStateException.class, () -> ticketService.createTicket(dto));
    }

    @Test
    void updateTicketStatus_ShouldUpdateSuccessfully() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(mapper.toTicketDTO(any(Ticket.class))).thenReturn(dto);

        TicketDTO result = ticketService.updateTicketStatus(1L, "RESOLVED");

        assertNotNull(result);
        verify(ticketRepository).save(any(Ticket.class));
        assertEquals("Test Ticket", result.title());
    }
}
