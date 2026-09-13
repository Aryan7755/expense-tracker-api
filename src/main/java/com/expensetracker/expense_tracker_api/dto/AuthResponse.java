package com.expensetracker.expense_tracker_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
}