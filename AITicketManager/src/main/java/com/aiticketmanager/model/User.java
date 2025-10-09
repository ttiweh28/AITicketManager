package com.aiticketmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "First name is required")
    private String fname;

    @NotBlank(message = "Last name is required")
    private String lname;

    @NotBlank(message = "Username cannot be empty")
    @Column(unique = true, nullable = false)
    private String userName;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @Email(message = "Email must be valid")
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Phone format: 555-555-5555")
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
}

