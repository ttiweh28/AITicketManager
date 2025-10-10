package com.aiticketmanager.service;

import com.aiticketmanager.dto.ManagerDTO;
import java.util.List;

public interface ManagerService {
    ManagerDTO getManagerById(Long id);
    List<ManagerDTO> getAllManagers();
}
