package com.aiticketmanager.repository;

import com.aiticketmanager.model.Ticket;
import com.aiticketmanager.model.enums.TicketCategory;
import com.aiticketmanager.model.enums.TicketPriority;
import com.aiticketmanager.model.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCustomer_UserId(Long customerId);

    List<Ticket> findByAgent_UserId(Long agentId);
    @Query("""
        SELECT t FROM Ticket t 
        WHERE t.agent.userId = :agentId
          AND (:category IS NULL OR t.category = :category)
          AND (:priority IS NULL OR t.priority = :priority)
          AND (:status IS NULL OR t.status = :status)
    """)
    List<Ticket> filterTicketsByAgent(
            @Param("agentId") Long agentId,
            @Param("category") TicketCategory category,
            @Param("priority") TicketPriority priority,
            @Param("status") TicketStatus status
    );

    @Query("SELECT t FROM Ticket t WHERE t.manager.userId = :managerId")
    List<Ticket> findTicketsManagedBy(Long managerId);

    List<Ticket> findBySubmissionDate(LocalDate date);

}
