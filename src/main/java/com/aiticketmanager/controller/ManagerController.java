package com.aiticketmanager.controller;

import com.aiticketmanager.dto.ManagerDTO;
import com.aiticketmanager.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
