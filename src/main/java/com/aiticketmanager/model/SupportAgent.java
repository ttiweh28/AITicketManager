package com.aiticketmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "support_agents")
public class SupportAgent extends User {

    @NotBlank(message = "Expertise field is required")
    private String expertise;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToMany(mappedBy = "agent")
    private List<Ticket> tickets = new ArrayList<>();
}
