package com.aiticketmanager.repository;

import com.aiticketmanager.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByUserName(String userName);
    boolean existsByEmail(String email);
}
