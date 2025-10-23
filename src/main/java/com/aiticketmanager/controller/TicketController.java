package com.aiticketmanager.controller;

import com.aiticketmanager.dto.TicketDTO;
import com.aiticketmanager.dto.TicketSummaryDTO;
import com.aiticketmanager.model.enums.TicketCategory;
import com.aiticketmanager.model.enums.TicketPriority;
import com.aiticketmanager.model.enums.TicketStatus;
import com.aiticketmanager.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


import java.util.List;

@Tag(name = "Ticket Management", description = "Create, update, and view support tickets")
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
        System.out.println("Authenticated user: " + SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println("Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

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

    @GetMapping("/agent/{agentId}/filter")
    public ResponseEntity<List<TicketSummaryDTO>> filterTicketsByAgent(
            @PathVariable Long agentId,
            @RequestParam(required = false) TicketCategory category,
            @RequestParam(required = false) TicketPriority priority,
            @RequestParam(required = false) TicketStatus status
    ) {
        return ResponseEntity.ok(ticketService.filterTicketsByAgent(agentId, category, priority, status));
    }

}
