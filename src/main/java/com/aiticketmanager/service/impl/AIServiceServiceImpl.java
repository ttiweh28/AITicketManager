package com.aiticketmanager.service.impl;

import com.aiticketmanager.model.AIService;
import com.aiticketmanager.repository.AIServiceRepository;
import com.aiticketmanager.service.AIServiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AIServiceServiceImpl implements AIServiceService {

    private final AIServiceRepository aiServiceRepository;

    @Override
    public List<AIService> getAllServices() {
        return aiServiceRepository.findAll();
    }
}
