package com.aiticketmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AIService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aiServiceId;

    @NotBlank(message = "Model name is required")
    private String modelName;

    @NotBlank(message = "Version is required")
    private String version;

    @OneToMany(mappedBy = "aiService")
    private List<Ticket> tickets = new ArrayList<>();
}

