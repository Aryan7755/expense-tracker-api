package com.expensetracker.expense_tracker_api.service;

import com.expensetracker.expense_tracker_api.dto.ExpenseRequestDTO;
import com.expensetracker.expense_tracker_api.dto.ExpenseResponseDTO;
import com.expensetracker.expense_tracker_api.entity.*;
import com.expensetracker.expense_tracker_api.repository.*;
import com.expensetracker.expense_tracker_api.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock private ExpenseRepository expenseRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private UserRepository userRepository;
    @Mock private BudgetRepository budgetRepository;
    @Mock private SecurityUtils securityUtils;

    @InjectMocks private ExpenseService expenseService;

    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).email("aryan@example.com").build();
        category = Category.builder().id(1L).name("Food").build();
    }

    @Test
    void createExpense_Success() {
        ExpenseRequestDTO dto = new ExpenseRequestDTO();
        dto.setTitle("Lunch");
        dto.setAmount(new BigDecimal("150.00"));
        dto.setCategoryId(1L);

        Expense savedExpense = Expense.builder()
                .id(100L)
                .title("Lunch")
                .amount(new BigDecimal("150.00"))
                .date(LocalDate.now())
                .user(user)
                .category(category)
                .build();

        when(securityUtils.getCurrentUserEmail()).thenReturn("aryan@example.com");
        when(userRepository.findByEmail("aryan@example.com")).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);

        ExpenseResponseDTO response = expenseService.createExpense(dto);

        assertNotNull(response);
        assertEquals("Lunch", response.getTitle());
        assertEquals("Food", response.getCategoryName());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }
}