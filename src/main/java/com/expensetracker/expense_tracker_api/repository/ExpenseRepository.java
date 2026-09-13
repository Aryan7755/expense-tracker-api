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

    @Query("SELECT e FROM Expense e WHERE e.user.id = ?1 " +
            "AND (?2 IS NULL OR e.category.id = ?2) " +
            "AND (?3 IS NULL OR e.date >= ?3) " +
            "AND (?4 IS NULL OR e.date <= ?4)")
    List<Expense> filterExpenses( Long userId,
                                  Long categoryId,
                                  LocalDate startDate,
                                  LocalDate endDate);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = ?1 " +
            "AND e.category.id = ?2 " +
            "AND e.date BETWEEN ?3 AND ?4")
    BigDecimal getTotalSpentInPeriod( Long userId,
                                      Long categoryId,
                                      LocalDate startDate,
                                      LocalDate endDate);
}