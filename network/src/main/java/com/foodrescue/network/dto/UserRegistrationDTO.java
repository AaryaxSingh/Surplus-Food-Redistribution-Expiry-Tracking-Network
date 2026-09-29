package com.foodrescue.network.dto;

import com.foodrescue.network.model.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegistrationDTO {

    @NotBlank(message = "name/buisness name is required")
    private String name;

    @Email(message = "valid email is required")
    @NotBlank(message = "email is required")
    private String email;

    @NotNull(message = "role is required")
    private UserRole role;

    private String address;
    private String phone;
    private Double latitude;
    private Double longitude;

}
