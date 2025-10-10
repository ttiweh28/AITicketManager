package com.aiticketmanager.repository;

import com.aiticketmanager.model.SupportAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportAgentRepository extends JpaRepository<SupportAgent, Long> {

    List<SupportAgent> findByManager_ManagerId(Long managerId);

    boolean existsByUser_UserName(String userName);
}
