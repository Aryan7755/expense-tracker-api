package com.expensetracker.expense_tracker_api.service;

import com.expensetracker.expense_tracker_api.dto.ExpenseRequestDTO;
import com.expensetracker.expense_tracker_api.entity.Category;
import com.expensetracker.expense_tracker_api.entity.Expense;
import com.expensetracker.expense_tracker_api.entity.User;
import com.expensetracker.expense_tracker_api.repository.CategoryRepository;
import com.expensetracker.expense_tracker_api.repository.ExpenseRepository;
import com.expensetracker.expense_tracker_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public Expense createExpense(ExpenseRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Expense expense = Expense.builder()
                .title(dto.getTitle())
                .amount(dto.getAmount())
                .date(dto.getDate() != null ? dto.getDate() : LocalDate.now())
                .description(dto.getDescription())
                .category(category)
                .user(user)
                .build();

        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpensesByUser(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}