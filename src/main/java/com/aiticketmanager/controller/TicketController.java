package com.aiticketmanager.controller;

import com.aiticketmanager.dto.TicketDTO;
import com.aiticketmanager.dto.TicketSummaryDTO;
import com.aiticketmanager.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody TicketDTO dto) {
        log.info("POST /api/tickets");
        TicketDTO saved = ticketService.createTicket(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<TicketSummaryDTO>> getTicketsByAgent(@PathVariable Long agentId) {
        return ResponseEntity.ok(ticketService.getTicketsByAgent(agentId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TicketSummaryDTO>> getTicketsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(ticketService.getTicketsByCustomer(customerId));
    }

    @PatchMapping("/{ticketId}/status")
    public ResponseEntity<TicketDTO> updateTicketStatus(
            @PathVariable Long ticketId,
            @RequestParam String status
    ) {
        TicketDTO updated = ticketService.updateTicketStatus(ticketId, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.noContent().build();
    }
}
