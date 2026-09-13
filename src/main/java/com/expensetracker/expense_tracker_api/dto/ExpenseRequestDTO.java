package com.expensetracker.expense_tracker_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private LocalDate date;
    private String description;

    @NotBlank(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "User ID is required")
    private Long userId;
}