package com.aiticketmanager.controller;

import com.aiticketmanager.dto.SupportAgentDTO;
import com.aiticketmanager.service.SupportAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@Tag(name = "Support Agent Management", description = "Manage and assign agents to tickets")
@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class SupportAgentController {

    private final SupportAgentService agentService;

    @GetMapping
    public ResponseEntity<List<SupportAgentDTO>> getAllAgents() {
        return ResponseEntity.ok(agentService.getAllAgents());
    }

    @PostMapping("/{agentId}/assign/{ticketId}")
    public ResponseEntity<SupportAgentDTO> assignTicket(
            @PathVariable Long agentId,
            @PathVariable Long ticketId
    ) {
        return ResponseEntity.ok(agentService.assignTicket(agentId, ticketId));
    }
}
