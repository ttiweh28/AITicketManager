package com.aiticketmanager.service.impl;

import com.aiticketmanager.dto.ManagerDTO;
import com.aiticketmanager.model.Manager;
import com.aiticketmanager.repository.ManagerRepository;
import com.aiticketmanager.service.ManagerService;
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
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final DtoMapper mapper;

    @Override
    public ManagerDTO getManagerById(Long id) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));
        return mapper.toManagerDTO(manager);
    }

    @Override
    public List<ManagerDTO> getAllManagers() {
        return managerRepository.findAll()
                .stream()
                .map(mapper::toManagerDTO)
                .collect(Collectors.toList());
    }
}
