package com.expensetracker.expense_tracker_api.service;

import com.expensetracker.expense_tracker_api.dto.ExpenseRequestDTO;
import com.expensetracker.expense_tracker_api.dto.ExpenseResponseDTO;
import com.expensetracker.expense_tracker_api.entity.*;
import com.expensetracker.expense_tracker_api.repository.*;
import com.expensetracker.expense_tracker_api.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Required Import
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final SecurityUtils securityUtils;

    public ExpenseResponseDTO createExpense(ExpenseRequestDTO dto) {
        String email = securityUtils.getCurrentUserEmail();
        log.info("Creating new expense for user: {} with title: {}", email, dto.getTitle());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        LocalDate expenseDate = dto.getDate() != null ? dto.getDate() : LocalDate.now();

        Expense expense = Expense.builder()
                .title(dto.getTitle())
                .amount(dto.getAmount())
                .date(expenseDate)
                .description(dto.getDescription())
                .category(category)
                .user(user)
                .build();

        Expense saved = expenseRepository.save(expense);
        log.info("Expense successfully created with ID: {}", saved.getId());

        String warning = checkBudgetLimit(user.getId(), category.getId(), expenseDate);
        if (warning != null) {
            log.warn("Budget limit warning for user {}: {}", user.getId(), warning);
        }

        return mapToResponse(saved, warning);
    }

    public List<ExpenseResponseDTO> getFilteredExpenses(Long categoryId, LocalDate startDate, LocalDate endDate) {
        String email = securityUtils.getCurrentUserEmail();
        log.info("Fetching filtered expenses for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return expenseRepository.filterExpenses(user.getId(), categoryId, startDate, endDate)
                .stream()
                .map(e -> mapToResponse(e, null))
                .collect(Collectors.toList());
    }

    private String checkBudgetLimit(Long userId, Long categoryId, LocalDate date) {
        Optional<Budget> budgetOpt = budgetRepository.findByUserIdAndCategoryId(userId, categoryId);
        if (budgetOpt.isPresent()) {
            YearMonth yearMonth = YearMonth.from(date);
            BigDecimal totalSpent = expenseRepository.getTotalSpentInPeriod(
                    userId, categoryId, yearMonth.atDay(1), yearMonth.atEndOfMonth());

            BigDecimal limit = budgetOpt.get().getMonthlyLimit();
            if (totalSpent != null && totalSpent.compareTo(limit) > 0) {
                return "WARNING: You have exceeded your monthly budget of " + limit + " for this category!";
            }
        }
        return null;
    }

    private ExpenseResponseDTO mapToResponse(Expense expense, String warning) {
        return ExpenseResponseDTO.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .amount(expense.getAmount())
                .date(expense.getDate())
                .description(expense.getDescription())
                .categoryName(expense.getCategory().getName())
                .budgetWarning(warning)
                .build();
    }
}