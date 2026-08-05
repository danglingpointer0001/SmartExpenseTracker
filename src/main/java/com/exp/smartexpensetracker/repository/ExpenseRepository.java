package com.exp.smartexpensetracker.repository;

import com.exp.smartexpensetracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import com.exp.smartexpensetracker.entity.User;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expense e")
    Double getTotalExpense();

    @Query("SELECT COALESCE(MAX(e.amount),0) FROM Expense e")
    Double getHighestExpense();

    @Query("SELECT COALESCE(MIN(e.amount),0) FROM Expense e")
    Double getLowestExpense();

    long count();
    List<Expense> findByTitleContainingIgnoreCase(String title);

    List<Expense> findByCategory(String category);

    List<Expense> findByExpenseDate(LocalDate date);
    List<Expense> findByUser(User user);

    List<Expense> findByUserAndTitleContainingIgnoreCase(User user, String title);

    List<Expense> findByUserAndCategory(User user, String category);

    List<Expense> findByUserAndExpenseDate(User user, LocalDate date);
}