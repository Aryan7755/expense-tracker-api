package com.expensetracker.expense_tracker_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Size(min = 2, max = 50) private String name;
    @Email @NotBlank private String email;
    @NotBlank private String password;
}