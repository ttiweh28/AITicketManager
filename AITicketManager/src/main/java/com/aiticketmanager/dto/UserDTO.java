package com.aiticketmanager.dto;

public record UserDTO (
        Long userId,
        String fname,
        String lname,
        String userName,
        String email,
        String phone,
        AddressDTO address
){ }
