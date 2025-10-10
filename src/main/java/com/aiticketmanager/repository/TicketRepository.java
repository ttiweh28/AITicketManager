package com.aiticketmanager.repository;

import com.aiticketmanager.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCustomer_CustomerId(Long customerId);

    List<Ticket> findByAgent_AgentId(Long agentId);

    @Query("SELECT t FROM Ticket t WHERE t.manager.managerId = :managerId")
    List<Ticket> findTicketsManagedBy(Long managerId);

    @Query("SELECT t FROM Ticket t WHERE t.submissionDate = :date")
    List<Ticket> findBySubmissionDate(LocalDate date);
}
