package com.aiticketmanager.model;

import com.aiticketmanager.model.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Category is required")
    private TicketCategory category;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Priority is required")
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private TicketStatus status = TicketStatus.OPEN;

    @NotNull
    private LocalDate submissionDate = LocalDate.now();


    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    @JsonIgnore
    private SupportAgent agent;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonIgnore
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "ai_service_id")
    private AIService aiService;
}
