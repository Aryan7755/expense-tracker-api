package com.expensetracker.expense_tracker_api.repository;

import com.expensetracker.expense_tracker_api.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUserIdAndCategoryId(Long userId, Long categoryId);
}