package com.aiticketmanager.repository;

import com.aiticketmanager.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCustomer_UserId(Long customerId);

    List<Ticket> findByAgent_UserId(Long agentId);

    @Query("SELECT t FROM Ticket t WHERE t.manager.userId = :managerId")
    List<Ticket> findTicketsManagedBy(Long managerId);

    List<Ticket> findBySubmissionDate(LocalDate date);
}
