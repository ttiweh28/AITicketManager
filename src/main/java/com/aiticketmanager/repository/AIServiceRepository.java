package com.aiticketmanager.repository;

import com.aiticketmanager.model.AIService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIServiceRepository extends JpaRepository<AIService, Long> {

    AIService findByModelName(String modelName);
}
