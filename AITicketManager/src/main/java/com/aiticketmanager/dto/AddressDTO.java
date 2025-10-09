package com.aiticketmanager.dto;


import jakarta.validation.constraints.NotBlank;

public record AddressDTO(
        @NotBlank String zip,
        @NotBlank String street,
        @NotBlank String state
) {}

