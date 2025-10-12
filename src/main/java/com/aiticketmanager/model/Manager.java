package com.aiticketmanager.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "managers")
public class Manager extends User {

    @OneToMany(mappedBy = "manager")
    private List<SupportAgent> agents = new ArrayList<>();

    @OneToMany(mappedBy = "manager")
    private List<Ticket> tickets = new ArrayList<>();
}
