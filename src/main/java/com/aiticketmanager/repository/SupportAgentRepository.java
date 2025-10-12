package com.aiticketmanager.repository;

import com.aiticketmanager.model.SupportAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupportAgentRepository extends JpaRepository<SupportAgent, Long> {
    Optional<SupportAgent> findByUserName(String userName);
    boolean existsByEmail(String email);
    List<SupportAgent> findByManager_ManagerId(Long managerId);

    boolean existsByUser_UserName(String userName);
}
