package com.aiticketmanager.controller;

import com.aiticketmanager.dto.ManagerDTO;
import com.aiticketmanager.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


import java.util.List;

@Tag(name = "Manager Management", description = "Retrieve and manage managers")
@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/{id}")
    public ResponseEntity<ManagerDTO> getManagerById(@PathVariable Long id) {
        return ResponseEntity.ok(managerService.getManagerById(id));
    }

    @GetMapping
    public ResponseEntity<List<ManagerDTO>> getAllManagers() {
        return ResponseEntity.ok(managerService.getAllManagers());
    }
}
