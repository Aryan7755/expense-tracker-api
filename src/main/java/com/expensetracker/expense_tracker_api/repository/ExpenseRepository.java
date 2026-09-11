package com.expensetracker.expense_tracker_api.repository;

import com.expensetracker.expense_tracker_api.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);

    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId " +
            "AND (:categoryId IS NULL OR e.category.id = :categoryId) " +
            "AND (:startDate IS NULL OR e.date >= :startDate) " +
            "AND (:endDate IS NULL OR e.date <= :endDate)")
    List<Expense> filterExpenses(@Param("userId") Long userId,
                                 @Param("categoryId") Long categoryId,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId " +
            "AND e.category.id = :categoryId " +
            "AND e.date BETWEEN :startDate AND :endDate")
    BigDecimal getTotalSpentInPeriod(@Param("userId") Long userId,
                                     @Param("categoryId") Long categoryId,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);
}